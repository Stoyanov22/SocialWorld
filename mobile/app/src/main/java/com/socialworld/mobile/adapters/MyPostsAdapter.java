package com.socialworld.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.ui.myPosts.MyPostsFragment.OnMyPostsInteractionListener;

/**
 * @author Atanas Katsarov
 */
public class MyPostsAdapter extends FirestoreRecyclerAdapter<PostEntity, MyPostsAdapter.MyPostsViewHolder> {
    private FirestoreRecyclerOptions<PostEntity> posts;
    private OnMyPostsInteractionListener mListener;

    public class MyPostsViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image;
        public TextView likesNum;

        public MyPostsViewHolder(@NonNull View itemView, final OnMyPostsInteractionListener listener) {
            super(itemView);

            text = itemView.findViewById(R.id.my_post_text);
            image = itemView.findViewById(R.id.my_post_pic);
            likesNum = itemView.findViewById(R.id.my_post_likes_num);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onOpenMyPostDetailsInteraction(getSnapshots().getSnapshot(position));
                    }
                }
            });
        }
    }

    public MyPostsAdapter(FirestoreRecyclerOptions<PostEntity> posts, OnMyPostsInteractionListener listener) {
        super(posts);
        this.posts = posts;

        mListener = listener;
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_my_post, parent, false);
        return new MyPostsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position, final PostEntity model) {
        holder.text.setText(model.getText());
        if (model.getUserLikes() != null) {
            holder.likesNum.setText(String.valueOf(model.getUserLikes().size()));
        }

        if (model.getPicture() != null) {
            GlideApp
                    .with(holder.image.getContext())
                    .load(model.getPicture())
                    .into(holder.image);
        } else {
            GlideApp
                    .with(holder.image.getContext())
                    .clear(holder.image);
        }
    }

}
