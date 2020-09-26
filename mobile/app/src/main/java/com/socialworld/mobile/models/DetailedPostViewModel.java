package com.socialworld.mobile.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.socialworld.mobile.entities.DetailedPost;

/**
 * @author Atanas Katsarov
 */
public class DetailedPostViewModel extends ViewModel {

    private MutableLiveData<DetailedPost> mPost;

    public DetailedPostViewModel() {
        mPost = new MutableLiveData<>();
    }

    public void setDetailedPost(DetailedPost post) {
        mPost.setValue(post);
    }

    public LiveData<DetailedPost> getPostLiveData() {
        return mPost;
    }

    public DetailedPost getDetailedPost() {
        return mPost.getValue();
    }
}