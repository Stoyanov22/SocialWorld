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
import com.socialworld.mobile.entities.NewsFeedPost;
import com.socialworld.mobile.models.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Atanas Katsarov
 */
public class NewsFeedPostsAdapter extends FirestorePagingAdapter<NewsFeedPost, NewsFeedPostsAdapter.NewsFeedPostViewHolder> {
    private FirestorePagingOptions<NewsFeedPost> posts;
    private OnNewsFeedPostClickListener mListener;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);

    public interface OnNewsFeedPostClickListener {
//        void onEditClick(int position);
    }

    public void setOnNewsFeedPostClickListener(OnNewsFeedPostClickListener listener) {
        mListener = listener;
    }

    public static class NewsFeedPostViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView text;
        public TextView date;
        public ImageView image;
        public ImageView profileImg;

        public NewsFeedPostViewHolder(@NonNull View itemView, final OnNewsFeedPostClickListener listener) {
            super(itemView);

            username = itemView.findViewById(R.id.feed_post_username);
            profileImg = itemView.findViewById(R.id.feed_post_profile_img);
            text = itemView.findViewById(R.id.feed_post_text);
            date = itemView.findViewById(R.id.feed_post_date);
            image = itemView.findViewById(R.id.feed_post_image);
        }
    }

    public NewsFeedPostsAdapter(FirestorePagingOptions<NewsFeedPost> posts) {
        super(posts);
        this.posts = posts;
    }

    @NonNull
    @Override
    public NewsFeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_news_feed_post, parent, false);

        return new NewsFeedPostViewHolder(view, mListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsFeedPostViewHolder holder, int position, @NonNull NewsFeedPost model) {
        if (model.getUsername() != null) {
            holder.username.setText(model.getUsername());
        } else {
            holder.username.setText(R.string.unknown_user);
        }
        holder.text.setText(model.getText());
        holder.date.setText(sdf.format(model.getDate()));
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

//    @Override
//    public Filter getFilter() {
//        return postsFilter;
//    }
//
//    private Filter postsFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            return null;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    };
}
