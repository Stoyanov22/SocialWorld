package com.socialworld.mobile.ui.myPosts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author Atanas Katsarov
 */
public class MyPostsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyPostsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}