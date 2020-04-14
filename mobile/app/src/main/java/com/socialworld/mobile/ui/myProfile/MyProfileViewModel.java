package com.socialworld.mobile.ui.myProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socialworld.mobile.entities.UserEntity;

public class MyProfileViewModel extends ViewModel {

    private MutableLiveData<UserEntity> mUser;

    public MyProfileViewModel() {
        mUser = new MutableLiveData<>();
    }

    public void setUser(UserEntity userEntity) {
        mUser.setValue(userEntity);
    }

    public LiveData<UserEntity> getUser() {
        return mUser;
    }
}