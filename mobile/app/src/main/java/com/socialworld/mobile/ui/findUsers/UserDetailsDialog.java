package com.socialworld.mobile.ui.findUsers;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.socialworld.mobile.R;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.models.GlideApp;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.socialworld.mobile.entities.CountryConstants.getCountryName;
import static com.socialworld.mobile.entities.GenderConstants.getGenderName;

/**
 * @author Atanas Katsarov
 */
public class UserDetailsDialog extends AppCompatDialogFragment {
    private UserEntity user;

    public UserDetailsDialog(UserEntity user) {
        this.user = user;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_user_details_dialog, null);
        ImageView profileImgView = view.findViewById(R.id.user_details_image);
        TextView nameTv = view.findViewById(R.id.user_details_name);
        TextView genderTv = view.findViewById(R.id.user_details_gender);
        TextView countryTv = view.findViewById(R.id.user_details_country);
        TextView dateOfBirthTv = view.findViewById(R.id.user_details_birthday);
        TextView numFollowersTv = view.findViewById(R.id.user_details_num_followers);
        TextView numFollowingTv = view.findViewById(R.id.user_details_num_following);
        if (user.getName() != null) {
            nameTv.setText(user.getName());
        }
        if (user.getGenderId() != null) {
            try {
                genderTv.setText(getGenderName(user.getGenderId()));
            } catch (Exception e) {
                Log.d("GENDER CONSTANT LOG", "Error with genders: " + e.getMessage());
            }
        }
        if (user.getCountryId() != null) {
            try {
                countryTv.setText(getCountryName(user.getCountryId()));
            } catch (Exception e) {
                Log.d("GENDER CONSTANT LOG", "Error with genders: " + e.getMessage());
            }
        }
        if (user.getDateOfBirth() != null) {
            final String myFormat = "dd/MM/yyyy";
            final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            dateOfBirthTv.setText(sdf.format(user.getDateOfBirth()));
        }
        if (user.getPicture() != null) {
            GlideApp
                    .with(requireContext())
                    .load(user.getPicture())
                    .centerCrop()
                    .into(profileImgView);
        }
        if (user.getFollowedUsers() != null) {
            numFollowingTv.setText(String.valueOf(user.getFollowedUsers().size()));
        }
        if (user.getFollowers() != null) {
            numFollowersTv.setText(String.valueOf(user.getFollowers().size()));
        }
        builder.setView(view)
                .setTitle(R.string.user_details);
        return builder.create();
    }
}
