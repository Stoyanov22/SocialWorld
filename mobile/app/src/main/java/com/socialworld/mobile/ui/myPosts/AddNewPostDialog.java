package com.socialworld.mobile.ui.myPosts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.socialworld.mobile.R;
import com.socialworld.mobile.models.GlideApp;

import com.socialworld.mobile.ui.myPosts.MyPostsFragment.OnMyPostsInteractionListener;

import static android.app.Activity.RESULT_OK;

public class AddNewPostDialog extends AppCompatDialogFragment {
    private static final int PICK_IMAGE = 1;

    private EditText postEditText;
    private ImageView postImgView;
    private Uri postImgUri;

    private static OnMyPostsInteractionListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_add_post_dialog, null);
        postEditText = view.findViewById(R.id.new_post_edit_text);
        postImgView = view.findViewById(R.id.new_post_image);
        final Drawable drawable = requireActivity().getDrawable(R.drawable.ic_image_file);

        builder.setView(view)
                .setTitle(R.string.add_new_post)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.add, null)
                .setNeutralButton(" ", null);

        final AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button neutralBtn = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
                if (drawable != null) {
                    drawable.setBounds((int) (drawable.getIntrinsicWidth() * 0.5),
                            0, (int) (drawable.getIntrinsicWidth() * 1.5),
                            drawable.getIntrinsicHeight());
                } else {
                    neutralBtn.setText(R.string.pic);
                }

                neutralBtn.setCompoundDrawables(drawable, null, null, null);
                neutralBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSelectPictureClicked();
                    }
                });

                Button addBtn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (postEditText.getText().length() == 0 && postImgUri == null) {
                            Toast.makeText(requireContext(), R.string.please_add_pic_or_text, Toast.LENGTH_LONG).show();
                            return;
                        }
                        onAddPostClicked();
                        dialog.dismiss();
                    }
                });
            }
        });
        return alert;
    }

    private void onSelectPictureClicked() {
        GlideApp
                .with(requireContext())
                .clear(postImgView);
        postImgView.setVisibility(View.INVISIBLE);
        postImgUri = null;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    private void onAddPostClicked() {
        final String postText = postEditText.getText().toString();

        if (mListener != null) {
            mListener.onCreateNewPostInteraction(postText, postImgUri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case PICK_IMAGE:
                postImgUri = data.getData();
                if (postImgUri != null) {
                    postImgView.setVisibility(View.VISIBLE);
                    GlideApp
                            .with(requireContext())
                            .load(postImgUri)
                            .into(postImgView);
                }
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnMyPostsInteractionListener) {
            mListener = (OnMyPostsInteractionListener) context;
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
}
