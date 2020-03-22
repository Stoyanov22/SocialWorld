package com.socialworld.mobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ForgotPasswordFragment extends Fragment {
    private static final String LAST_EMAIL = "last_email";

    private String lastEmail;

    private OnForgottenPasswordInteractionListener mListener;

    EditText emailEditText;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    public static ForgotPasswordFragment newInstance(String lastEmailText) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString(LAST_EMAIL, lastEmailText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lastEmail = getArguments().getString(LAST_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        if (lastEmail != null) {
            emailEditText = view.findViewById(R.id.email);
            emailEditText.setText(lastEmail);
        }

        view.findViewById(R.id.btn_reset_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordPressed(emailEditText.getText().toString());
            }
        });

        return view;
    }

    public void onResetPasswordPressed(String email) {
        if (mListener != null) {
            mListener.onForgottenPasswordInteraction(email);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgottenPasswordInteractionListener) {
            mListener = (OnForgottenPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgottenPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForgottenPasswordInteractionListener {
        void onForgottenPasswordInteraction(String email);
    }
}
