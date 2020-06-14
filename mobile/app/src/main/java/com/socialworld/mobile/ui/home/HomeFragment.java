package com.socialworld.mobile.ui.home;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.socialworld.mobile.R;
import com.socialworld.mobile.adapters.PostsAdapter;
import com.socialworld.mobile.entities.NewsFeedPost;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.entities.UserCollection;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.FollowedUsersViewModel;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

public class HomeFragment extends Fragment {
    private RecyclerView postsRecView;
    private RecyclerView.LayoutManager postsLayoutManager;
    private PostsAdapter postsAdapter;
    private MyProfileViewModel myProfileViewModel;
    private FollowedUsersViewModel followedUsersViewModel;

    private FirebaseFirestore db;

//    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        followedUsersViewModel = new ViewModelProvider(requireActivity()).get(FollowedUsersViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        postsRecView = view.findViewById(R.id.home_posts_rec_view);
        postsRecView.setHasFixedSize(true);
        postsLayoutManager = new LinearLayoutManager(requireContext());
        postsRecView.setLayoutManager(postsLayoutManager);

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();
        followedUsersViewModel.getFollowedUsersLiveData().observe(getViewLifecycleOwner(), new Observer<UserCollection>() {
            @Override
            public void onChanged(final UserCollection userCollection) {
                if (userCollection != null) {
                    // Query from FirebaseFirestore
                    Query query = db.collection("Posts").whereIn("userId", userCollection.getUserIdsList()).orderBy("date", Query.Direction.DESCENDING);
                    // Options
                    FirestorePagingOptions<NewsFeedPost> options = new FirestorePagingOptions.Builder<NewsFeedPost>()
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .setQuery(query, config, new SnapshotParser<NewsFeedPost>() {
                                @NonNull
                                @Override
                                public NewsFeedPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    PostEntity post = snapshot.toObject(PostEntity.class);
                                    return new NewsFeedPost(post, userCollection.getUserMap().get(post.getUserId()));
                                }
                            })
                            .build();
                    // Adapter
                    postsAdapter = new PostsAdapter(options);
                    postsRecView.setAdapter(postsAdapter);
                    postsRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                }
            }
        });
//        myProfileViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<UserEntity>() {
//            @Override
//            public void onChanged(UserEntity userEntity) {
//                if (userEntity != null && userEntity.getFollowedUsers() != null && userEntity.getFollowedUsers().size() > 0) {
//                    // Query from FirebaseFirestore
//                    Query query = db.collection("Posts").whereIn("userId", userEntity.getFollowedUsers()).orderBy("date", Query.Direction.DESCENDING);
//                    // Options
//                    FirestorePagingOptions<PostEntity> options = new FirestorePagingOptions.Builder<PostEntity>()
//                            .setLifecycleOwner(getViewLifecycleOwner())
//                            .setQuery(query, config, new SnapshotParser<PostEntity>() {
//                                @NonNull
//                                @Override
//                                public PostEntity parseSnapshot(@NonNull DocumentSnapshot snapshot) {
//                                    PostEntity post = snapshot.toObject(PostEntity.class);
//
//                                    return post;
//                                }
//                            })
//                            .build();
//                    // Adapter
//                    postsAdapter = new PostsAdapter(options);
//                    postsRecView.setAdapter(postsAdapter);
//                    postsRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
//                }
//            }
//        });
//        .setQuery(query, config, new SnapshotParser<PostEntity>() {
//            @NonNull
//            @Override
//            public PostEntity parseSnapshot(@NonNull DocumentSnapshot snapshot) {
//                PostEntity post = snapshot.toObject(PostEntity.class);
//                return post;
//            }
//        })

//        postsAdapter.setOnPostItemClickListener(new PostsAdapter.OnPostItemClickListener() {
//            @Override
//            public void onEditClick(int position) {
//
//            }
//        });

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

    public interface OnNewsFeedInteractionListener {
    }
}
