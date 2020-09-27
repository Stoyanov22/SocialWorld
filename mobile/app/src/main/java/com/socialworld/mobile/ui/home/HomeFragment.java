package com.socialworld.mobile.ui.home;

import android.content.Context;
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
import com.socialworld.mobile.adapters.NewsFeedPostsAdapter;
import com.socialworld.mobile.entities.DetailedPost;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.entities.UserCollection;
import com.socialworld.mobile.models.FollowedUsersViewModel;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class HomeFragment extends Fragment {
    private TextView notFollowingTv;
    private RecyclerView postsRecView;
    private RecyclerView.LayoutManager postsLayoutManager;
    private NewsFeedPostsAdapter newsFeedPostsAdapter;
//    private MyProfileViewModel myProfileViewModel;
    private FollowedUsersViewModel followedUsersViewModel;

    private OnPostInteractionListener mListener;

    private FirebaseFirestore db;

//    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        followedUsersViewModel = new ViewModelProvider(requireActivity()).get(FollowedUsersViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        notFollowingTv = view.findViewById(R.id.home_not_following_label);
        postsRecView = view.findViewById(R.id.home_posts_rec_view);
        postsRecView.setHasFixedSize(true);
        postsLayoutManager = new LinearLayoutManager(requireContext());
        postsRecView.setLayoutManager(postsLayoutManager);

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(3)
                .setPageSize(3)
                .build();
        followedUsersViewModel.getFollowedUsersLiveData().observe(getViewLifecycleOwner(), new Observer<UserCollection>() {
            @Override
            public void onChanged(final UserCollection userCollection) {
                if (userCollection != null && userCollection.getUserMap().size() > 0) {
                    notFollowingTv.setVisibility(View.INVISIBLE);
                    // Query from FirebaseFirestore
                    Query query = db.collection("Posts").whereIn("userId", userCollection.getUserIdsList()).orderBy("date", Query.Direction.DESCENDING);
                    // Options
                    FirestorePagingOptions<DetailedPost> options = new FirestorePagingOptions.Builder<DetailedPost>()
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .setQuery(query, config, new SnapshotParser<DetailedPost>() {
                                @NonNull
                                @Override
                                public DetailedPost parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                    PostEntity post = snapshot.toObject(PostEntity.class);
                                    return new DetailedPost(post, userCollection.getUserMap().get(post.getUserId()));
                                }
                            })
                            .build();
                    // Adapter
                    newsFeedPostsAdapter = new NewsFeedPostsAdapter(options, mListener);
                    postsRecView.setAdapter(newsFeedPostsAdapter);
                    postsRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                } else {
                    notFollowingTv.setVisibility(View.VISIBLE);
                }
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnPostInteractionListener) {
            mListener = (OnPostInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPostInteractionListener {
        void onDeletePostInteraction(String postId);

        void onAddCommentInteraction(String text, String postId);

        void onDeleteCommentInteraction(String commentId);

        void onOpenPostDetailsInteraction(DocumentSnapshot postSnapshot);

        void onLikeOfPostInteraction(PostEntity post, boolean isLiked);

        boolean isPostLikedInteraction(List<String> userLikes);
    }
}
