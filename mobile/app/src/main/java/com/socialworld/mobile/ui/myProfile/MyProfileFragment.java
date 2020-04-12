package com.socialworld.mobile.ui.myProfile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel myProfileViewModel;

    private OnMyProfileInteractionListener mListener;

    private TextView nameTv;
    private TextView countryTv;
    private TextView dateOfBirthTv;
    private TextView emailTv;
    private TextView numFollowersTv;
    private TextView numFollowing;

    private UserEntity user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);

        user = onLoadMyProfile();
        myProfileViewModel.setUser(user);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);

        nameTv = root.findViewById(R.id.my_profile_name);
        countryTv = root.findViewById(R.id.my_profile_country);
        dateOfBirthTv = root.findViewById(R.id.my_profile_birthday);
        emailTv = root.findViewById(R.id.my_profile_email);
        numFollowersTv = root.findViewById(R.id.my_profile_num_followers);
        numFollowing = root.findViewById(R.id.my_profile_num_following);

        myProfileViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity.getName() != null) {
                    nameTv.setText(userEntity.getName());
                }
                if (userEntity.getCountryId() != null) {
                    countryTv.setText(userEntity.getCountryId());
                }
                if (userEntity.getDateOfBirth() != null) {
                    dateOfBirthTv.setText(userEntity.getDateOfBirth());
                }
                if (userEntity.getEmail() != null) {
                    emailTv.setText(userEntity.getEmail());
                }
                // TODO get the count of followers and following users
                numFollowersTv.setText("100");
                numFollowing.setText("50");
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyProfileInteractionListener) {
            mListener = (OnMyProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public UserEntity onLoadMyProfile() {
        return mListener.onLoadMyProfileListener();
    }

    public interface OnMyProfileInteractionListener {
        UserEntity onLoadMyProfileListener();
    }
}
