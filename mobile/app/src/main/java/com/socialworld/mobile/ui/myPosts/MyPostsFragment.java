package com.socialworld.mobile.ui.myPosts;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.PostEntity;

public class MyPostsFragment extends Fragment {
//    private FirebaseFirestore db;

    private MyPostsViewModel myPostsViewModel;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        db = FirebaseFirestore.getInstance();
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPostsViewModel = new ViewModelProvider(requireActivity()).get(MyPostsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        final TextView textView = view.findViewById(R.id.text_my_posts);
        myPostsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        FloatingActionButton fab = view.findViewById(R.id.new_post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewPostDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    private void openAddNewPostDialog() {
        AddNewPostDialog newPostDialog = new AddNewPostDialog();
        newPostDialog.show(requireActivity().getSupportFragmentManager(), "New Post");
    }

    public interface OnMyPostsInteractionListener {
        void onCreateNewPostInteraction(final String text, Uri imgUri);

        void onCreateNewPostInteraction(PostEntity post);
    }
}
