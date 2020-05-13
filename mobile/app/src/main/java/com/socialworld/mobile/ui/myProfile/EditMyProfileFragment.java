package com.socialworld.mobile.ui.myProfile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static androidx.navigation.Navigation.findNavController;

public class EditMyProfileFragment extends Fragment {

    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

    private MyProfileViewModel myProfileViewModel;
    private ImageView profileImage;
    private TextView changePhotoTv;
    private EditText nameEditTxt;

    private Calendar birthCalendar;
    private EditText dateOfBirthEditTxt;

    private Spinner countrySpinner;

    private StorageReference profilePicRef;

    private OnUpdateMyProfileInteractionListener mListener;

    public EditMyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);

        profilePicRef = FirebaseStorage.getInstance().getReference().child("ProfilePictures");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false);
        profileImage = view.findViewById(R.id.edit_profile_image);
        changePhotoTv = view.findViewById(R.id.change_photo_text);
        nameEditTxt = view.findViewById(R.id.edit_text_name);
        dateOfBirthEditTxt = view.findViewById(R.id.edit_date_of_birth);
        countrySpinner = view.findViewById(R.id.edit_country_spinner);

        final List<String> countryOptions = new ArrayList<>();
        for (String country : Locale.getISOCountries()) {
            Locale locale = new Locale("en", country);
            countryOptions.add(locale.getDisplayCountry());
        }
        final ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, countryOptions);
        countrySpinner.setAdapter(countryAdapter);

        final String myFormat = "dd/MM/yyyy";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        birthCalendar = Calendar.getInstance();

        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    if (userEntity.getName() != null) {
                        nameEditTxt.setText(userEntity.getName());
                    }
                    if (userEntity.getDateOfBirth() != null) {
                        birthCalendar.setTime(userEntity.getDateOfBirth());
                        dateOfBirthEditTxt.setText(sdf.format(birthCalendar.getTime()));
                    }
                    if (userEntity.getCountryCode() != null) {
                        countrySpinner.setSelection(countryAdapter.getPosition(userEntity.getCountryCode()));
                    }
                }
            }
        });


        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                birthCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                dateOfBirthEditTxt.setText(sdf.format(birthCalendar.getTime()));
            }

        };

        dateOfBirthEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireActivity(), dateSetListener,
                        birthCalendar.get(Calendar.YEAR),
                        birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePhoto();
            }
        });
        changePhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePhoto();
            }
        });
        Button updateBtn = view.findViewById(R.id.btn_update_profile);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileButtonPressed();
                findNavController(v).navigate(R.id.action_nav_edit_profile_to_nav_my_profile);
            }
        });

        return view;
    }

    private void changeProfilePhoto() {
        final CharSequence[] options = {"Capture Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Capture Photo")) {
                    Intent capturePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capturePhoto, CAPTURE_IMAGE);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateProfileButtonPressed() {
        UserEntity user = myProfileViewModel.getUser();
        user.setName(nameEditTxt.getText().toString());
        user.setDateOfBirth(birthCalendar.getTime());
        user.setCountryCode(countrySpinner.getSelectedItem().toString());
//        user.setPicture("");
        if (mListener != null) {
            mListener.onUpdateMyProfileInteraction();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case CAPTURE_IMAGE:
                Bitmap image = (Bitmap) data.getExtras().get("data");
//                Uri tempUri = getImageUri(requireContext(), image);
                profileImage.setImageBitmap(image);
                break;
            case PICK_IMAGE:
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    profilePicRef.child(myProfileViewModel.getUser().getEmail()).putFile(imageUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Profile picture was saved", Toast.LENGTH_LONG).show();
                                task.getResult().getStorage().getDownloadUrl().toString();
                            }
                        }
                    });
                    profileImage.setImageURI(imageUri);
                }
                break;
        }
    }

//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
//        return Uri.parse(path);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateMyProfileInteractionListener) {
            mListener = (OnUpdateMyProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgottenPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUpdateMyProfileInteractionListener {
        void onUpdateMyProfileInteraction();
    }

}
