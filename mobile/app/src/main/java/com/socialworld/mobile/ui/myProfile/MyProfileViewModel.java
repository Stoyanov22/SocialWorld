package com.socialworld.mobile.ui.myProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socialworld.mobile.entities.UserEntity;

/**
 * @author Atanas Katsarov
 */
public class MyProfileViewModel extends ViewModel {

    private MutableLiveData<UserEntity> mUser;

    public MyProfileViewModel() {
        mUser = new MutableLiveData<>();
    }

    public void setUser(UserEntity userEntity) {
        mUser.setValue(userEntity);
    }

    public LiveData<UserEntity> getUserLiveData() {
        return mUser;
    }

    public UserEntity getUser() {
        return mUser.getValue();
    }
}