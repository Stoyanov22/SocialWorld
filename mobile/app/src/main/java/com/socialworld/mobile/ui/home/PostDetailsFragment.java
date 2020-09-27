package com.socialworld.mobile.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.CommentEntity;
import com.socialworld.mobile.entities.DetailedPost;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.models.DetailedPostViewModel;
import com.socialworld.mobile.ui.home.HomeFragment.OnPostInteractionListener;
import com.socialworld.mobile.ui.myProfile.MyProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Atanas Katsarov
 */
public class PostDetailsFragment extends Fragment {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
    private MyProfileViewModel myProfileViewModel;
    private DetailedPostViewModel detailedPostViewModel;
    private FirestoreRecyclerOptions<CommentEntity> options;
    private FirestoreRecyclerAdapter<CommentEntity, CommentViewHolder> commentsAdapter;

    private boolean isMyPost = false;
    private OnPostInteractionListener mListener;

    private FirebaseFirestore db;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        myProfileViewModel = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        detailedPostViewModel = new ViewModelProvider(requireActivity()).get(DetailedPostViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        final ImageView profileImgView = view.findViewById(R.id.post_profile_img);
        final TextView usernameTv = view.findViewById(R.id.post_username);
        final ImageView deleteImgView = view.findViewById(R.id.post_delete);
        final ImageView postImgView = view.findViewById(R.id.post_pic);
        final TextView postTextTv = view.findViewById(R.id.post_text);
        final TextView postLikesNumTv = view.findViewById(R.id.post_likes_num);
        final TextView postDateTv = view.findViewById(R.id.post_date);
        final RecyclerView commentsRecView = view.findViewById(R.id.post_comments_rec_view);
        final EditText addCommentEditText = view.findViewById(R.id.post_add_comment_edit_text);
        final Button addCommentBtn = view.findViewById(R.id.post_add_comment_btn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        detailedPostViewModel.getPostLiveData().observe(getViewLifecycleOwner(), new Observer<DetailedPost>() {
            @Override
            public void onChanged(final DetailedPost post) {
                if (post != null) {
                    isMyPost = (post.getUserId() != null && myProfileViewModel.getUser() != null && post.getUserId().equals(myProfileViewModel.getUser().getId()));
                    if (post.getProfilePic() != null) {
                        GlideApp
                                .with(requireContext())
                                .load(post.getProfilePic())
                                .circleCrop()
                                .into(profileImgView);
                    }
                    if (post.getUsername() != null) {
                        usernameTv.setText(post.getUsername());
                    }
                    if (isMyPost) {
                        deleteImgView.setVisibility(View.VISIBLE);
                        deleteImgView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                builder.setMessage(R.string.delete_post_warning)
                                        .setTitle(R.string.delete_post);

                                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mListener.onDeletePostInteraction(post.getId());
                                    }
                                });
                                builder.setNegativeButton(R.string.no, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                    if (post.getPicture() != null) {
                        GlideApp
                                .with(requireContext())
                                .load(post.getPicture())
                                .centerCrop()
                                .into(postImgView);
                    }
                    if (post.getText() != null) {
                        postTextTv.setText(post.getText());
                    }
                    if (post.getUserLikes() != null) {
                        postLikesNumTv.setText(String.valueOf(post.getUserLikes().size()));
                    }
                    if (post.getDate() != null) {
                        postDateTv.setText(sdf.format(post.getDate()));
                    }
                    // Query from FirebaseFirestore
                    final Query query = db.collection("Comments").whereEqualTo("postId", post.getId()).orderBy("date", Query.Direction.ASCENDING);
                    // Options
                    options = new FirestoreRecyclerOptions.Builder<CommentEntity>()
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .setQuery(query, CommentEntity.class)
                            .build();

                    commentsAdapter = new FirestoreRecyclerAdapter<CommentEntity, CommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position, @NonNull CommentEntity model) {
                            db.collection("Users").document(model.getUserId()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            holder.username.setText(documentSnapshot.toObject(UserEntity.class).getName());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("POST_DETAIL_LOG", "Error when trying to get username: " + e.getMessage());
                                        }
                                    });
                            holder.commentText.setText(model.getText());
                            if (isMyPost) {
                                holder.deleteBtn.setVisibility(View.VISIBLE);
                                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                                        builder.setMessage(R.string.delete_comment_warning)
                                                .setTitle(R.string.delete_comment);

                                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                mListener.onDeleteCommentInteraction(getSnapshots().getSnapshot(position).getId());
                                                commentsAdapter.updateOptions(options);
                                            }
                                        });
                                        builder.setNegativeButton(R.string.no, null);

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });
                            }
                        }

                        @NonNull
                        @Override
                        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_comment, parent, false));
                        }
                    };

                    commentsRecView.setAdapter(commentsAdapter);
                    commentsRecView.setHasFixedSize(false);
                    commentsRecView.setLayoutManager(layoutManager);
                    commentsRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

                    addCommentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (addCommentEditText.getText().length() == 0) {
                                Toast.makeText(requireContext(), R.string.please_input_text, Toast.LENGTH_LONG).show();
                                return;
                            }

                            mListener.onAddCommentInteraction(addCommentEditText.getText().toString(), post.getId());
                            addCommentEditText.getText().clear();
                            commentsAdapter.updateOptions(options);
                        }
                    });
                }
            }
        });

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

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView commentText;
        public ImageView deleteBtn;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.comment_user_label);
            commentText = itemView.findViewById(R.id.comment_text_label);
            deleteBtn = itemView.findViewById(R.id.comment_delete);
        }
    }
}
