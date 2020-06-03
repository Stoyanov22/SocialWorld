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
import com.socialworld.mobile.entities.PostEntity;
import com.socialworld.mobile.models.GlideApp;

// TODO: Use FirestorePagingAdapter
public class PostAdapter extends FirestorePagingAdapter<PostEntity, PostAdapter.PostViewHolder>{
    private FirestorePagingOptions<PostEntity> posts;
    private OnPostItemClickListener mListener;

    public interface OnPostItemClickListener {
//        void onEditClick(int position);
    }

    public void setOnPostItemClickListener(OnPostItemClickListener listener) {
        mListener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView text;
        public TextView date;
        public ImageView image;
        public PostViewHolder(@NonNull View itemView, final OnPostItemClickListener listener) {
            super(itemView);

            username = itemView.findViewById(R.id.post_item_username);
            text = itemView.findViewById(R.id.post_item_text);
            date = itemView.findViewById(R.id.post_item_date);
            image = itemView.findViewById(R.id.post_item_image);
        }
    }

    public PostAdapter(FirestorePagingOptions<PostEntity> posts) {
        super(posts);
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_post, parent, false);

        return new PostViewHolder(view, mListener);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostEntity model) {
        holder.username.setText(model.getUserId());
        holder.text.setText(model.getText());
        holder.date.setText(model.getDate().toString());
        if (model.getPicture() != null) {
            GlideApp
                    .with(holder.image.getContext())
                    .load(model.getPicture())
                    .into(holder.image);
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
