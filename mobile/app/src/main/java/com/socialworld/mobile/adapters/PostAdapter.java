package com.socialworld.mobile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.PostEntity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements Filterable {
    private List<PostEntity> posts;
    private OnPostItemClickListener mListener;

    public interface OnPostItemClickListener {
        void onEditClick(int position);
    }

    public void setOnPostItemClickListener(OnPostItemClickListener listener) {
        mListener = listener;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public PostViewHolder(@NonNull View itemView, final OnPostItemClickListener listener) {
            super(itemView);

            text = itemView.findViewById(android.R.id.text1);
        }
    }

    public PostAdapter(List<PostEntity> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new PostViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostEntity post = posts.get(position);

        holder.text.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public Filter getFilter() {
        return postsFilter;
    }

    private Filter postsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };
}
