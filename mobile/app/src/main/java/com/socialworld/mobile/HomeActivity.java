package com.socialworld.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.socialworld.mobile.entities.CommentEntity;
import com.socialworld.mobile.entities.DetailedPost;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.FollowedUsersViewModel;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.ui.findUsers.UserDetailsDialog;
import com.socialworld.mobile.ui.home.HomeFragment;
import com.socialworld.mobile.ui.myPosts.MyPostsFragment;
import com.socialworld.mobile.models.DetailedPostViewModel;
import com.socialworld.mobile.ui.myProfile.MyProfileFragment;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static androidx.navigation.Navigation.findNavController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class HomeActivity extends AppCompatActivity implements HomeFragment.OnPostInteractionListener, MyPostsFragment.OnMyPostsInteractionListener, MyProfileFragment.OnMyProfileInteractionListener {
    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    //    private UserEntity user;
    private String userUid;

    private AlertDialog loadingDialog;

    private ImageView navImgView;
    private TextView navUsernameTv;
    private TextView navEmailTv;

    private MyProfileViewModel myProfileViewModel;
    private FollowedUsersViewModel followedUsersViewModel;

    private DetailedPostViewModel detailedPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            goToAuthorizeActivity();
        } else {
            AlertDialog.Builder builderLoading = new AlertDialog.Builder(HomeActivity.this);
            builderLoading.setCancelable(false);
            builderLoading.setView(R.layout.loading_dialog);
            loadingDialog = builderLoading.create();

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_my_posts, R.id.nav_my_profile, R.id.nav_find_users)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            View navHeaderView = navigationView.getHeaderView(0);
            navImgView = navHeaderView.findViewById(R.id.nav_header_profile_pic);
            navUsernameTv = navHeaderView.findViewById(R.id.nav_header_username);
            navEmailTv = navHeaderView.findViewById(R.id.nav_header_profile_email);

            userUid = firebaseAuth.getCurrentUser().getUid();
            db = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
            followedUsersViewModel = new ViewModelProvider(this).get(FollowedUsersViewModel.class);

            detailedPostViewModel = new ViewModelProvider(this).get(DetailedPostViewModel.class);

            db.collection("Users").document(userUid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final UserEntity user = documentSnapshot.toObject(UserEntity.class);
                            if (user == null) {
                                return;
                            }
                            // if user was disabled and on login it is enabled again
                            myProfileViewModel.setUser(user);
                            if (!myProfileViewModel.getUser().isEnabled()) {
                                myProfileViewModel.getUser().setEnabled(true);
                                onUpdateMyProfileInteraction();
                            }
                            if (user.getFollowedUsers() != null && user.getFollowedUsers().size() > 0) {
                                db.collection("Users").whereEqualTo("enabled", true).whereIn("id", user.getFollowedUsers()).get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<UserEntity> userList = queryDocumentSnapshots.toObjects(UserEntity.class);
                                                followedUsersViewModel.setFollowedUsers(userList);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("PROFILE_LOG", "Error when trying to get followed users info: " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("PROFILE_LOG", "Error when trying to get profile information: " + e.getMessage());
                        }
                    });

            myProfileViewModel.getUserLiveData().observe(this, new Observer<UserEntity>() {
                @Override
                public void onChanged(UserEntity userEntity) {
                    if (userEntity != null) {
                        updateNavigationHeader(userEntity);
                    }
                }
            });
        }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showLoading() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    private void hideLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void updateNavigationHeader(UserEntity user) {
        if (user.getName() != null) {
            navUsernameTv.setText(user.getName());
        }
        if (user.getEmail() != null) {
            navEmailTv.setText(user.getEmail());
        }
        if (user.getPicture() != null) {
            GlideApp
                    .with(getApplicationContext())
                    .load(user.getPicture())
                    .circleCrop()
                    .into(navImgView);
        }
    }

    private void goToAuthorizeActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthorizeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateMyProfileInteraction(Uri imgUri) {
        showLoading();
        firebaseStorage.getReference().child("ProfilePictures").child(userUid).putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            myProfileViewModel.getUser().setPicture(task.getResult().toString());
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.failed_upload_profile_pic, Toast.LENGTH_LONG).show();
                                        }
                                        hideLoading();
                                        onUpdateMyProfileInteraction();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.failed_upload_profile_pic, Toast.LENGTH_LONG).show();
                        hideLoading();
                        onUpdateMyProfileInteraction();
                    }
                });
    }

    @Override
    public void onUpdateMyProfileInteraction() {
        updateNavigationHeader(myProfileViewModel.getUser());
        db.collection("Users").document(userUid).set(myProfileViewModel.getUser(), SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("PROFILE_LOG", "Profile updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("PROFILE_LOG", "Error when trying to update profile information: " + e.getMessage());
                    }
                });
    }

    @Override
    public void onLogoutUserInteraction() {
        firebaseAuth.signOut();
        goToAuthorizeActivity();
    }

    @Override
    public void onDisableProfileInteraction() {
        showLoading();
        myProfileViewModel.getUser().setEnabled(false);
        db.collection("Users").document(userUid).set(myProfileViewModel.getUser(), SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.disabled_profile_notification, Toast.LENGTH_LONG).show();
                        onLogoutUserInteraction();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onFollowUserInteraction(List<UserEntity> followedUsers, UserEntity user) {
        db.collection("Users").document(user.getId()).set(user);
        followedUsersViewModel.setFollowedUsers(followedUsers);
        myProfileViewModel.getUser().setFollowedUsers(followedUsersViewModel.getFollowedUsers().getUserIdsList());
        onUpdateMyProfileInteraction();
    }

    @Override
    public void onShowUserDetailsInteraction(UserEntity user) {
        UserDetailsDialog dialog = new UserDetailsDialog(user);
        dialog.show(getSupportFragmentManager(), "User Details");
    }

    @Override
    public String onGetMyUserUid() {
        return userUid;
    }

    @Override
    public void onCreateNewPostInteraction(final String text, Uri imgUri) {
        showLoading();
        final long time = System.currentTimeMillis();
        final String postId = userUid + time;

        if (imgUri != null) {
            firebaseStorage.getReference().child("Posts").child(userUid).child(postId).putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            onCreateNewPostInteraction(new PostEntity(postId, userUid, uri.toString(), text, new Date()));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            hideLoading();
                                            Log.d("POST_LOG", "Error when trying to create new post: " + e.getMessage());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideLoading();
                            Log.d("POST_LOG", "Error when trying to create new post: " + e.getMessage());
                        }
                    });
        } else {
            onCreateNewPostInteraction(new PostEntity(postId, userUid, text, new Date()));
        }
    }

    @Override
    public void onCreateNewPostInteraction(PostEntity post) {
        showLoading();
        db.collection("Posts").document(post.getId()).set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.post_added, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Log.d("POST_LOG", "Error when trying to create new post: " + e.getMessage());
                    }
                });
    }

    @Override
    public void onOpenMyPostDetailsInteraction(DocumentSnapshot postSnapshot) {
        PostEntity post = postSnapshot.toObject(PostEntity.class);
        if (post != null) {
            detailedPostViewModel.setDetailedPost(new DetailedPost(post, myProfileViewModel.getUser()));
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_nav_my_posts_to_nav_post_details);
        }
    }

    @Override
    public void onDeletePostInteraction(String postId) {
        showLoading();
        db.collection("Posts").document(postId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        findNavController(HomeActivity.this, R.id.nav_host_fragment).popBackStack();
                        hideLoading();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        hideLoading();
                    }
                });
    }

    @Override
    public void onAddCommentInteraction(String text, String postId) {
        final long time = System.currentTimeMillis();
        final String commentId = userUid + time;

        db.collection("Comments").document(commentId).set(new CommentEntity(commentId, userUid, postId, text, new Date()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("COMMENT_LOG", "Comment added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("COMMENT_LOG", "Error when trying to create new comment: " + e.getMessage());
                        hideLoading();
                    }
                });
    }

    @Override
    public void onDeleteCommentInteraction(String commentId) {
        showLoading();
        db.collection("Comments").document(commentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("COMMENT_LOG", "Comment deleted");
                        hideLoading();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        hideLoading();
                    }
                });
    }

    @Override
    public void onOpenPostDetailsInteraction(DocumentSnapshot postSnapshot) {
        PostEntity post = postSnapshot.toObject(PostEntity.class);
        if (post != null) {
            detailedPostViewModel.setDetailedPost(new DetailedPost(post, followedUsersViewModel.getFollowedUsers().getUserMap().get(post.getUserId())));
            findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_nav_home_to_post_details);
        }
    }

    @Override
    public void onLikeOfPostInteraction(PostEntity post, boolean isLiked) {
        if (post != null) {
            if (post.getUserLikes() == null) {
                post.setUserLikes(new ArrayList<String>());
            }
            if (isLiked) {
                if (!post.getUserLikes().contains(userUid)) {
                    post.getUserLikes().add(userUid);
                }
            } else {
                post.getUserLikes().remove(userUid);
            }
            db.collection("Posts").document(post.getId()).set(post)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("POST_LIKE", "Post liked/unliked");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("POST_LOG", "Error when trying like post: " + e.getMessage());
                        }
                    });
        }
    }

    @Override
    public boolean isPostLikedInteraction(List<String> userLikes) {
        return userLikes.contains(userUid);
    }
}