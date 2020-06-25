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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Atanas Katsarov
 */
public class MyPostsAdapter extends FirestoreRecyclerAdapter<PostEntity, MyPostsAdapter.MyPostsViewHolder> {
    private FirestoreRecyclerOptions<PostEntity> posts;
    private OnMyPostsClickListener mListener;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);

    public interface OnMyPostsClickListener {

    }

    public void setOnMyPostsClickListener(OnMyPostsClickListener listener) {
        mListener = listener;
    }

    public static class MyPostsViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public TextView date;
        public ImageView image;

        public MyPostsViewHolder(@NonNull View itemView, final OnMyPostsClickListener listener) {
            super(itemView);

            text = itemView.findViewById(R.id.my_post_text);
            date = itemView.findViewById(R.id.my_post_date);
            image = itemView.findViewById(R.id.my_post_pic);
        }
    }

    public MyPostsAdapter(FirestoreRecyclerOptions<PostEntity> posts) {
        super(posts);
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_my_post, parent, false);
        return new MyPostsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position, PostEntity model) {
        holder.text.setText(model.getText());
        holder.date.setText(sdf.format(model.getDate()));

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
