package com.socialworld.mobile.ui.myPosts;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socialworld.mobile.R;
import com.socialworld.mobile.adapters.NewsFeedPostsAdapter;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

/**
 * @author Atanas Katsarov
 */
public class MyPostsFragment extends Fragment {
    private RecyclerView myPostsRecView;
    private RecyclerView.LayoutManager myPostsLayoutManager;
    private NewsFeedPostsAdapter myNewsFeedPostsAdapter;
    private MyProfileViewModel myProfileViewModel;

    private FirebaseFirestore db;

    private MyPostsViewModel myPostsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);

        db = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        myPostsRecView = view.findViewById(R.id.my_posts_rec_view);
        myPostsRecView.setHasFixedSize(true);
        myPostsLayoutManager = new LinearLayoutManager(requireContext());
        myPostsRecView.setLayoutManager(myPostsLayoutManager);

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();

        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null && userEntity.getId() != null) {
//                    // Query from FirebaseFirestore
//                    Query query = db.collection("Posts").whereEqualTo("userId",userEntity.getId()).orderBy("date", Query.Direction.DESCENDING);
//                    // Options
//                    FirestorePagingOptions<PostEntity> options = new FirestorePagingOptions.Builder<PostEntity>()
//                            .setLifecycleOwner(getViewLifecycleOwner())
//                            .setQuery(query, config, PostEntity.class)
//                            .build();
//                    // Adapter
//                    myPostsAdapter = new PostsAdapter(options);
//                    myPostsRecView.setAdapter(myPostsAdapter);
//                    myPostsRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                }
            }
        });
//        myPostsViewModel = new ViewModelProvider(requireActivity()).get(MyPostsViewModel.class);
//        myPostsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//            }
//        });


        FloatingActionButton fab = view.findViewById(R.id.new_post_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewPostDialog();
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
