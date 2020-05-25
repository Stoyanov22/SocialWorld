package com.socialworld.mobile.ui.myPosts;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.PostEntity;

public class MyPostsFragment extends Fragment implements AddNewPostDialog.OnAddNewPostInteractionListener {
    private FirebaseFirestore db;

    private MyPostsViewModel myPostsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPostsViewModel = new ViewModelProvider(requireActivity()).get(MyPostsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        final TextView textView = view.findViewById(R.id.text_gallery);
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

    public void openAddNewPostDialog() {
        AddNewPostDialog newPostDialog = new AddNewPostDialog();
        newPostDialog.show(requireActivity().getSupportFragmentManager(), "New Post");
    }

    @Override
    public void addNewPost(PostEntity post) {
        db.collection("Posts").document(post.getId()).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Post added", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        loadingDialog.hide();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
