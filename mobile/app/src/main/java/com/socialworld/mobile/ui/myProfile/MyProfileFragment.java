package com.socialworld.mobile.ui.myProfile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;

import static androidx.navigation.Navigation.findNavController;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel myProfileViewModel;

    private TextView nameTv;
    private TextView countryTv;
    private TextView dateOfBirthTv;
//    private TextView emailTv;
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

        nameTv = root.findViewById(R.id.my_profile_name);
        countryTv = root.findViewById(R.id.my_profile_country);
        dateOfBirthTv = root.findViewById(R.id.my_profile_birthday);
//        emailTv = root.findViewById(R.id.my_profile_email);
        numFollowersTv = root.findViewById(R.id.my_profile_num_followers);
        numFollowingTv = root.findViewById(R.id.my_profile_num_following);

        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    if (userEntity.getName() != null) {
                        nameTv.setText(userEntity.getName());
                    }
                    if (userEntity.getCountryId() != null) {
                        countryTv.setText(userEntity.getCountryId());
                    }
                    if (userEntity.getDateOfBirth() != null) {
                        dateOfBirthTv.setText(userEntity.getDateOfBirth().toString());
                    }
//                    if (userEntity.getEmail() != null) {
//                        emailTv.setText(userEntity.getEmail());
//                    }
                    // TODO get the count of followers and following users
                    numFollowersTv.setText("100");
                    numFollowingTv.setText("50");
                }
            }
        });

        editProfileBtn = root.findViewById(R.id.edit_profile_btn);

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
}
