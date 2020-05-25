package com.socialworld.mobile.ui.home;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.socialworld.mobile.R;
import com.socialworld.mobile.adapters.PostAdapter;
import com.socialworld.mobile.entities.PostEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private RecyclerView postsRecView;
    private RecyclerView.LayoutManager postsLayoutManager;
    private PostAdapter postAdapter;
    private List<PostEntity> posts;

    private FirebaseFirestore db;

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postsRecView = view.findViewById(R.id.home_posts_rec_view);
        postsRecView.setHasFixedSize(true);
        postsLayoutManager = new LinearLayoutManager(requireContext());
        postsRecView.setLayoutManager(postsLayoutManager);

        posts = new ArrayList<>();
        db.collection("Posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot postSnapshot : Objects.requireNonNull(task.getResult())) {
                            posts.add(postSnapshot.toObject(PostEntity.class));
                        }
                        postAdapter = new PostAdapter(posts);
                        postAdapter.setOnPostItemClickListener(new PostAdapter.OnPostItemClickListener() {
                            @Override
                            public void onEditClick(int position) {

                            }
                        });

                        postsRecView.setAdapter(postAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
//        final TextView textView = view.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return view;
    }
}
