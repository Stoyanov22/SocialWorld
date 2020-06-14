package com.socialworld.mobile.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socialworld.mobile.entities.UserCollection;
import com.socialworld.mobile.entities.UserEntity;

import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class FollowedUsersViewModel extends ViewModel {
    private MutableLiveData<UserCollection> mFollowedUsers;

    public FollowedUsersViewModel() {
        mFollowedUsers = new MutableLiveData<>();
    }

    public void setFollowedUsers(UserCollection userCollection) {
        mFollowedUsers.setValue(userCollection);
    }

    public void setFollowedUsers(List<UserEntity> userList) {
        UserCollection userCollection = new UserCollection(userList);
        mFollowedUsers.setValue(userCollection);
    }

    public LiveData<UserCollection> getFollowedUsersLiveData() {
        return mFollowedUsers;
    }

    public UserCollection getFollowedUsers() {
        return mFollowedUsers.getValue();
    }
}
