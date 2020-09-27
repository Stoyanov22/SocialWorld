package com.socialworld.mobile.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.DetailedPost;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.ui.home.HomeFragment.OnPostInteractionListener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Atanas Katsarov
 */
public class NewsFeedPostsAdapter extends FirestorePagingAdapter<DetailedPost, NewsFeedPostsAdapter.NewsFeedPostViewHolder> {
    private FirestorePagingOptions<DetailedPost> posts;
    private OnPostInteractionListener mListener;
    private Set<String> postsLikedIds;
    private Set<String> postsUnlikedIds;

    public static class NewsFeedPostViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView text;
        public ImageView image;
        public ImageView profileImg;
        public ImageView likeImg;
        public TextView likesNum;

        public NewsFeedPostViewHolder(@NonNull View itemView, final OnPostInteractionListener listener) {
            super(itemView);

            username = itemView.findViewById(R.id.feed_post_username);
            profileImg = itemView.findViewById(R.id.feed_post_profile_img);
            text = itemView.findViewById(R.id.feed_post_text);
            image = itemView.findViewById(R.id.feed_post_image);
            likeImg = itemView.findViewById(R.id.feed_like_img);
            likesNum = itemView.findViewById(R.id.feed_likes_num);
        }
    }

    public NewsFeedPostsAdapter(FirestorePagingOptions<DetailedPost> posts, OnPostInteractionListener listener) {
        super(posts);
        this.posts = posts;
        mListener = listener;
        postsLikedIds = new HashSet<>();
        postsUnlikedIds = new HashSet<>();
    }

    @NonNull
    @Override
    public NewsFeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_news_feed_post, parent, false);

        return new NewsFeedPostViewHolder(view, mListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NewsFeedPostViewHolder holder, final int position, @NonNull final DetailedPost model) {
        if (model.getUsername() != null) {
            holder.username.setText(model.getUsername());
        } else {
            holder.username.setText(R.string.unknown_user);
        }
        holder.text.setText(model.getText());
        if (model.getProfilePic() != null) {
            GlideApp
                    .with(holder.profileImg.getContext())
                    .load(model.getProfilePic())
                    .circleCrop()
                    .into(holder.profileImg);
        } else {
            GlideApp
                    .with(holder.image.getContext())
                    .clear(holder.profileImg);
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
        View.OnClickListener postClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOpenPostDetailsInteraction(getItem(position));
            }
        };
        holder.image.setOnClickListener(postClickListener);
        holder.text.setOnClickListener(postClickListener);

        int numberOfLikes = 0;
        if (model.getUserLikes() != null) {
            numberOfLikes = model.getUserLikes().size();
            final boolean isLikedBefore = mListener.isPostLikedInteraction(model.getUserLikes());
            if (postsLikedIds.contains(model.getId())) {
                numberOfLikes++;
                holder.likeImg.setImageResource(R.drawable.ic_like_marked);
            } else if (postsUnlikedIds.contains(model.getId())) {
                numberOfLikes--;
                holder.likeImg.setImageResource(R.drawable.ic_like_unmarked);
            } else if (isLikedBefore) {
                holder.likeImg.setImageResource(R.drawable.ic_like_marked);
            } else {
                holder.likeImg.setImageResource(R.drawable.ic_like_unmarked);
            }

            holder.likeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int numLikes = model.getUserLikes().size();
                    boolean isLiked;
                    postsLikedIds.remove(model.getId());
                    postsUnlikedIds.remove(model.getId());
                    if (mListener.isPostLikedInteraction(model.getUserLikes())) {
                        isLiked = false;
                        numLikes--;
                        if (isLikedBefore) {
                            postsUnlikedIds.add(model.getId());
                        }
                        holder.likeImg.setImageResource(R.drawable.ic_like_unmarked);
                    } else {
                        isLiked = true;
                        numLikes++;
                        if (!isLikedBefore) {
                            postsLikedIds.add(model.getId());
                        }
                        holder.likeImg.setImageResource(R.drawable.ic_like_marked);
                    }

                    if (numLikes < 0) {
                        numLikes = 0;
                    }
                    mListener.onLikeOfPostInteraction(model, isLiked);
                    holder.likesNum.setText(String.valueOf(numLikes));
                }
            });
        }
        holder.likesNum.setText(String.valueOf(numberOfLikes));
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {
            case LOADING_INITIAL:
                Log.d("PAGING_LOG", "Loading initial");
                break;
            case LOADING_MORE:
                Log.d("PAGING_LOG", "Loading more");
                break;
            case FINISHED:
                Log.d("PAGING_LOG", "All Data loaded");
                break;
            case LOADED:
                Log.d("PAGING_LOG", "Loaded: " + getItemCount());
                break;
            case ERROR:
                Log.d("PAGING_LOG", "Error");
                break;
        }
    }
}
