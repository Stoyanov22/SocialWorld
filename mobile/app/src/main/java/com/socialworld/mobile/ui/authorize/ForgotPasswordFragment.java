package com.socialworld.mobile.ui.authorize;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socialworld.mobile.R;

/**
 * @author Atanas Katsarov
 */
public class ForgotPasswordFragment extends Fragment {
    private OnForgottenPasswordInteractionListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        view.findViewById(R.id.btn_reset_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordPressed();
            }
        });

        return view;
    }

    private void onResetPasswordPressed() {
        if (mListener != null) {
            mListener.onForgottenPasswordInteraction();
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
        void onForgottenPasswordInteraction();
    }
}
