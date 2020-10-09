package com.socialworld.mobile.ui.findUsers;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.socialworld.mobile.R;
import com.socialworld.mobile.adapters.UsersAdapter;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.FollowedUsersViewModel;
import com.socialworld.mobile.ui.myProfile.MyProfileFragment.OnMyProfileInteractionListener;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class FindUsersFragment extends Fragment {
    private MyProfileViewModel myProfileViewModel;
    private FollowedUsersViewModel followedUsersViewModel;
    private OnMyProfileInteractionListener mListener;
    private RecyclerView usersRecView;
    private SearchView searchView;
    private UsersAdapter adapter;
    private FirebaseFirestore db;
    private RadioGroup filterRadioGroup;
    private RadioButton followedRadioBtn;
    private RadioButton unfollowedRadioBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        followedUsersViewModel = new ViewModelProvider(requireActivity()).get(FollowedUsersViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_users, container, false);
        filterRadioGroup = view.findViewById(R.id.filter_radio_group);
        followedRadioBtn = view.findViewById(R.id.radio_followed);
        unfollowedRadioBtn = view.findViewById(R.id.radio_unfollowed);
        searchView = view.findViewById(R.id.find_users_search);
        searchView.setVisibility(View.INVISIBLE);
        filterRadioGroup.setVisibility(View.INVISIBLE);

        usersRecView = view.findViewById(R.id.find_users_rec_view);
        usersRecView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        usersRecView.setLayoutManager(layoutManager);

        db.collection("Users").whereEqualTo("enabled", true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<UserEntity> followedUsers = new ArrayList<>();
                        if (followedUsersViewModel.getFollowedUsers() != null) {
                            followedUsers.addAll(followedUsersViewModel.getFollowedUsers().getUserSet());
                        }
                        List<UserEntity> unfollowedUsers = queryDocumentSnapshots.toObjects(UserEntity.class);
                        unfollowedUsers.remove(myProfileViewModel.getUser());
                        unfollowedUsers.removeAll(followedUsers);
                        adapter = new UsersAdapter(unfollowedUsers, followedUsers, mListener);
                        usersRecView.setAdapter(adapter);
                        usersRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

                        searchView.setVisibility(View.VISIBLE);
                        filterRadioGroup.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USERS_LOG", "Couldn't load users: " + e.getMessage());
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshOptionsInAdapter(newText);
                return false;
            }
        });
        View.OnClickListener radioButtonClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshOptionsInAdapter(searchView.getQuery().toString());
            }
        };
        followedRadioBtn.setOnClickListener(radioButtonClickedListener);
        unfollowedRadioBtn.setOnClickListener(radioButtonClickedListener);
        return view;
    }

    private void refreshOptionsInAdapter(String searchString) {
        boolean isLookingForFollowed = filterRadioGroup.getCheckedRadioButtonId() == followedRadioBtn.getId();
        adapter.filterOptionsByString(searchString, isLookingForFollowed);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyProfileInteractionListener) {
            mListener = (OnMyProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyProfileInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}