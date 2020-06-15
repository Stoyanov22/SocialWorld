package com.socialworld.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.FollowedUsersViewModel;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.ui.home.HomeFragment;
import com.socialworld.mobile.ui.myPosts.MyPostsFragment;
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

import java.util.Date;
import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class HomeActivity extends AppCompatActivity implements HomeFragment.OnNewsFeedInteractionListener, MyPostsFragment.OnMyPostsInteractionListener, MyProfileFragment.OnMyProfileInteractionListener {
    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    private UserEntity user;
    private String userUid;

    private AlertDialog loadingDialog;

    private ImageView navImgView;
    private TextView navUsernameTv;
    private TextView navEmailTv;

    private MyProfileViewModel myProfileViewModel;
    private FollowedUsersViewModel followedUsersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), AuthorizeActivity.class);
            startActivity(intent);
            finish();
        }

        userUid = firebaseAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        followedUsersViewModel = new ViewModelProvider(this).get(FollowedUsersViewModel.class);

        View navView = getLayoutInflater().inflate(R.layout.nav_header_home, null);
        navImgView = navView.findViewById(R.id.nav_header_profile_pic);
        navUsernameTv = navView.findViewById(R.id.nav_header_username);
        navEmailTv = navView.findViewById(R.id.nav_header_profile_email);

        db.collection("Users").document(userUid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserEntity.class);
                        myProfileViewModel.setUser(user);
                        if (user.getFollowedUsers() != null && user.getFollowedUsers().size() > 0) {
                            db.collection("Users").whereIn("id", user.getFollowedUsers()).get()
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
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        myProfileViewModel.getUserLiveData().observe(this, new Observer<UserEntity>() {
            @Override
            public void onChanged(UserEntity userEntity) {
                if (userEntity != null) {
                    if (userEntity.getName() != null) {
                        navUsernameTv.setText(userEntity.getName());
                    }
                    if (userEntity.getEmail() != null) {
                        navEmailTv.setText(userEntity.getEmail());
                    }
                    if (userEntity.getPicture() != null) {
                        GlideApp
                                .with(getApplicationContext())
                                .load(userEntity.getPicture())
                                .into(navImgView);
                    }
                }
            }
        });

        AlertDialog.Builder builderLoading = new AlertDialog.Builder(HomeActivity.this);
        builderLoading.setCancelable(false);
        builderLoading.setView(R.layout.loading_dialog);
        loadingDialog = builderLoading.create();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_my_posts, R.id.nav_my_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
                                        onUpdateMyProfileInteraction();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.failed_upload_profile_pic, Toast.LENGTH_LONG).show();
                        onUpdateMyProfileInteraction();
                    }
                });
    }

    @Override
    public void onUpdateMyProfileInteraction() {
        showLoading();
        db.collection("Users").document(userUid).set(myProfileViewModel.getUser(), SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.profile_updated, Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideLoading();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
