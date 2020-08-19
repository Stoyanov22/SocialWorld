package com.socialworld.mobile.ui.myProfile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import com.socialworld.mobile.ui.myProfile.MyProfileFragment.OnMyProfileInteractionListener;

import static android.app.Activity.RESULT_OK;
import static androidx.navigation.Navigation.findNavController;
import static com.socialworld.mobile.entities.CountryConstants.getCountriesMap;
import static com.socialworld.mobile.entities.GenderConstants.getGendersMap;

/**
 * @author Atanas Katsarov
 */
public class EditMyProfileFragment extends Fragment {

    private static final int CAPTURE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

    private MyProfileViewModel myProfileViewModel;
    private ImageView profileImgView;
    private TextView changePhotoTv;
    private EditText nameEditTxt;

    private Calendar birthCalendar;
    private EditText dateOfBirthEditTxt;

    private Spinner genderSpinner;
    private Spinner countrySpinner;

    private Uri imageUri;

    private OnMyProfileInteractionListener mListener;

    public EditMyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_my_profile, container, false);
        profileImgView = view.findViewById(R.id.edit_profile_image);
        changePhotoTv = view.findViewById(R.id.change_photo_text);
        nameEditTxt = view.findViewById(R.id.edit_text_name);
        dateOfBirthEditTxt = view.findViewById(R.id.edit_date_of_birth);
        genderSpinner = view.findViewById(R.id.edit_gender_spinner);
        countrySpinner = view.findViewById(R.id.edit_country_spinner);

//        int indexCountry = 0;
//        for (String country : Locale.getISOCountries()) {
//            Locale locale = new Locale("en", country);
//            countryOptions.add(locale.getDisplayCountry());
//            countryCodesMap.put(indexCountry, locale.getCountry());
//            indexCountry++;
//        }
        final SortedMap<Integer, String> genderMap = new TreeMap<>(getGendersMap());
        final List<String> genderOptions = new ArrayList<>(genderMap.values());
        final ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, genderOptions);
        genderSpinner.setAdapter(genderAdapter);
        final SortedMap<Integer, String> countriesMap = new TreeMap<>(getCountriesMap());
        final List<String> countryOptions = new ArrayList<>(countriesMap.values());
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
                    if (userEntity.getGenderId() != null) {
                        genderSpinner.setSelection(userEntity.getGenderId());
                    }
                    if (userEntity.getCountryId() != null) {
                        countrySpinner.setSelection(userEntity.getCountryId());
                    }
                    if (userEntity.getPicture() != null) {
                        GlideApp
                                .with(requireContext())
                                .load(userEntity.getPicture())
                                .centerCrop()
                                .into(profileImgView);
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

        profileImgView.setOnClickListener(new View.OnClickListener() {
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
                if (nameEditTxt.getText().length() == 0) {
                    Toast.makeText(requireContext(), R.string.username_can_not_be_empty, Toast.LENGTH_LONG).show();
                    return;
                }
                updateProfileButtonPressed();
                findNavController(v).navigate(R.id.action_nav_edit_profile_to_nav_my_profile);
            }
        });

        return view;
    }

    private void changeProfilePhoto() {
        final CharSequence[] options = {getResources().getString(R.string.capture_photo), getResources().getString(R.string.choose_from_gallery), getResources().getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.change_profile_pic);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(options[0])) {
                    Intent capturePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(capturePhoto, CAPTURE_IMAGE);
                } else if (options[which].equals(options[1])) {
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
        user.setGenderId(genderSpinner.getSelectedItemPosition());
        user.setCountryId(countrySpinner.getSelectedItemPosition());
        if (mListener != null) {
            if (imageUri != null) {
                user.setPicture(imageUri.toString());
                mListener.onUpdateMyProfileInteraction(imageUri);
            } else {
                mListener.onUpdateMyProfileInteraction();
            }
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
                if (image != null) {
                    imageUri = getImageUri(image);
                }
                break;
            case PICK_IMAGE:
                imageUri = data.getData();
                break;
        }
        loadPictureInImageView();
    }

    private void loadPictureInImageView() {
        if (imageUri != null) {
            GlideApp
                    .with(requireContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(profileImgView);
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyProfileInteractionListener) {
            mListener = (OnMyProfileInteractionListener) context;
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
}
