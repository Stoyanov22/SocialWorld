package com.socialworld.mobile.ui.myPosts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.PostEntity;

import java.util.Date;

public class AddNewPostDialog extends AppCompatDialogFragment {
    private EditText postEditText;
    private OnAddNewPostInteractionListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_post_dialog, null);

        builder.setView(view)
                .setTitle("New Post")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String postText = postEditText.getText().toString();

                        PostEntity post = new PostEntity("id","userId","pic", postText, new Date());

                        mListener.addNewPost(post);
                    }
                });

        postEditText = view.findViewById(R.id.new_post_text);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnAddNewPostInteractionListener) {
            mListener = (OnAddNewPostInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddNewPostInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddNewPostInteractionListener {
        void addNewPost(PostEntity post);
    }
}
