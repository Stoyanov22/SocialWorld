package com.socialworld.mobile.ui.findUsers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;

/**
 * @author Atanas Katsarov
 */
public class FindUsersFragment extends Fragment {

    private RecyclerView usersRecView;
    private EditText searchEditText;
    private Query query;
    private FirestorePagingOptions<UserEntity> options;
    private FirestorePagingAdapter<UserEntity, FoundUsersViewHolder> adapter;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_users, container, false);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        usersRecView = view.findViewById(R.id.find_users_rec_view);
        usersRecView.setHasFixedSize(true);
        usersRecView.setLayoutManager(layoutManager);

        final PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(20)
                .setPageSize(10)
                .build();

        query = db.collection("Users");
        options = new FirestorePagingOptions.Builder<UserEntity>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(query, config, UserEntity.class)
                .build();

        adapter = new FirestorePagingAdapter<UserEntity, FoundUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoundUsersViewHolder holder, int position, @NonNull UserEntity model) {
                holder.name.setText(model.getName());
                if (model.getPicture() != null) {
                    GlideApp
                            .with(holder.image.getContext())
                            .load(model.getPicture())
                            .circleCrop()
                            .into(holder.image);

                } else {
                    GlideApp
                            .with(holder.image.getContext())
                            .clear(holder.image);
                }
            }

            @NonNull
            @Override
            public FoundUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user, parent, false);
                return new FoundUsersViewHolder(view);
            }
        };
        usersRecView.setAdapter(adapter);
        usersRecView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        searchEditText = view.findViewById(R.id.find_users_search);
        ImageButton searchBtn = view.findViewById(R.id.find_users_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEditText.getText().length() == 0) {
                    Toast.makeText(requireContext(), R.string.please_input_username, Toast.LENGTH_LONG).show();
                    return;
                }
                query = db.collection("Users").whereEqualTo("name", searchEditText.getText().toString());
                options = new FirestorePagingOptions.Builder<UserEntity>()
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .setQuery(query, config, UserEntity.class)
                        .build();
                adapter.updateOptions(options);
            }
        });

        return view;
    }

    private class FoundUsersViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private Button followBtn;

        public FoundUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.user_item_img);
            name = itemView.findViewById(R.id.user_item_name);
            followBtn = itemView.findViewById(R.id.user_item_follow_btn);
        }
    }
}
