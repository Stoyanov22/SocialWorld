package com.socialworld.mobile.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;
import com.socialworld.mobile.ui.myProfile.MyProfileFragment.OnMyProfileInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> implements Filterable {
    private OnMyProfileInteractionListener mListener;
    private List<UserEntity> usersUnfollowedList;
    private List<UserEntity> usersFollowedList;
    private List<UserEntity> usersFullList;
    private List<UserEntity> filteredUsersList;
    private boolean isFollowedUsers = false;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user, parent, false);
        return new UserViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserEntity user = filteredUsersList.get(position);
        holder.name.setText(user.getName());
        if (user.getPicture() != null) {
            GlideApp
                    .with(holder.image.getContext())
                    .load(user.getPicture())
                    .circleCrop()
                    .into(holder.image);

        } else {
            GlideApp
                    .with(holder.image.getContext())
                    .clear(holder.image);
        }

        if (isFollowedUsers) {
            holder.followBtn.setText(R.string.unfollow);
        } else {
            holder.followBtn.setText(R.string.follow);
        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private Button followBtn;

        public UserViewHolder(@NonNull View itemView, final OnMyProfileInteractionListener listener) {
            super(itemView);

            image = itemView.findViewById(R.id.user_item_img);
            name = itemView.findViewById(R.id.user_item_name);
            followBtn = itemView.findViewById(R.id.user_item_follow_btn);

            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    UserEntity user = filteredUsersList.get(position);
                    filteredUsersList.remove(position);
                    if (isFollowedUsers) {
                        usersFollowedList.remove(user);
                        user.getFollowers().remove(listener.onGetMyUserUid());
                        usersUnfollowedList.add(user);
                    } else {
                        user.getFollowers().add(listener.onGetMyUserUid());
                        usersFollowedList.add(user);
                        usersUnfollowedList.remove(user);
                    }
                    listener.onFollowUserInteraction(usersFollowedList, user);
                    notifyItemRemoved(position);
                }
            });
        }
    }

    public UsersAdapter(List<UserEntity> usersUnfollowedList, List<UserEntity> usersFollowedList, OnMyProfileInteractionListener listener) {
        super();

        this.filteredUsersList = usersUnfollowedList;
        this.usersFullList = new ArrayList<>(usersUnfollowedList);
        this.usersUnfollowedList = new ArrayList<>(usersUnfollowedList);
        this.usersFollowedList = new ArrayList<>(usersFollowedList);
        this.mListener = listener;
    }

    public void filterOptionsByString(String searchString, boolean isFollowedUsers) {
        this.isFollowedUsers = isFollowedUsers;
        if (this.isFollowedUsers) {
            usersFullList = new ArrayList<>(usersFollowedList);
        } else {
            usersFullList = new ArrayList<>(usersUnfollowedList);
        }
        usersFilter.filter(searchString);
    }

    @Override
    public int getItemCount() {
        return filteredUsersList.size();
    }

    @Override
    public Filter getFilter() {
        return usersFilter;
    }

    private Filter usersFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserEntity> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(usersFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (UserEntity site : usersFullList) {
                    if (site.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(site);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredUsersList.clear();
            filteredUsersList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
