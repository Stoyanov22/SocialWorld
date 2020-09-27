package com.socialworld.mobile.ui.myProfile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static androidx.navigation.Navigation.findNavController;
import static com.socialworld.mobile.entities.CountryConstants.getCountryName;
import static com.socialworld.mobile.entities.GenderConstants.getGenderName;

/**
 * @author Atanas Katsarov
 */
public class MyProfileFragment extends Fragment {

    private MyProfileViewModel myProfileViewModel;
    private OnMyProfileInteractionListener mListener;

    private ImageView profileImgView;
    private TextView nameTv;
    private TextView genderTv;
    private TextView countryTv;
    private TextView dateOfBirthTv;
    private TextView numFollowersTv;
    private TextView numFollowingTv;
    private Button editProfileBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        profileImgView = root.findViewById(R.id.my_profile_image);
        nameTv = root.findViewById(R.id.my_profile_name);
        genderTv = root.findViewById(R.id.my_profile_gender);
        countryTv = root.findViewById(R.id.my_profile_country);
        dateOfBirthTv = root.findViewById(R.id.my_profile_birthday);
        numFollowersTv = root.findViewById(R.id.my_profile_num_followers);
        numFollowingTv = root.findViewById(R.id.my_profile_num_following);

        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    if (userEntity.getName() != null) {
                        nameTv.setText(userEntity.getName());
                    }
                    if (userEntity.getGenderId() != null) {
                        try {
                            genderTv.setText(getGenderName(userEntity.getGenderId()));
                        } catch (Exception e) {
                            Log.d("GENDER CONSTANT LOG", "Error with genders: " + e.getMessage());
                        }
                    }
                    if (userEntity.getCountryId() != null) {
                        try {
                            countryTv.setText(getCountryName(userEntity.getCountryId()));
                        } catch (Exception e) {
                            Log.d("GENDER CONSTANT LOG", "Error with genders: " + e.getMessage());
                        }
                    }
                    if (userEntity.getDateOfBirth() != null) {
                        final String myFormat = "dd/MM/yyyy";
                        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                        dateOfBirthTv.setText(sdf.format(userEntity.getDateOfBirth()));
                    }
                    if (userEntity.getPicture() != null) {
                        GlideApp
                                .with(requireContext())
                                .load(userEntity.getPicture())
                                .centerCrop()
                                .into(profileImgView);
                    }
                    if (userEntity.getFollowedUsers() != null) {
                        numFollowingTv.setText(String.valueOf(userEntity.getFollowedUsers().size()));
                    }
                    if (userEntity.getFollowers() != null) {
                        numFollowersTv.setText(String.valueOf(userEntity.getFollowers().size()));
                    }
                }
            }
        });

        editProfileBtn = root.findViewById(R.id.edit_profile_btn);

        root.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLogoutUserInteraction();
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(v).navigate(R.id.action_nav_my_profile_to_nav_edit_profile);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyProfileInteractionListener) {
            mListener = (OnMyProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyProfileInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMyProfileInteractionListener {
        void onUpdateMyProfileInteraction(Uri imgUri);

        void onUpdateMyProfileInteraction();

        void onLogoutUserInteraction();

        void onFollowUserInteraction(List<UserEntity> followedUsers, UserEntity userFollowed);

        String onGetMyUserUid();
    }
}
