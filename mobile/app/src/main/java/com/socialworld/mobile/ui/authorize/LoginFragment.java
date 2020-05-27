package com.socialworld.mobile.ui.authorize;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.socialworld.mobile.R;


public class LoginFragment extends Fragment {
    private EditText passwordEditText;

    private OnLoginInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        passwordEditText = view.findViewById(R.id.password);

        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginPressed(passwordEditText.getText().toString());
            }
        });

        view.findViewById(R.id.btn_go_to_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToRegisterPressed();
            }
        });

        view.findViewById(R.id.label_forgotten_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToForgottenPasswordPressed();
            }
        });
        return view;
    }

    private void onLoginPressed(String password) {
        if (mListener != null) {
            mListener.onLoginInteraction(password);
        }
    }

    private void onGoToRegisterPressed() {
        if (mListener != null) {
            mListener.onGoToRegisterInteraction();
        }
    }

    private void onGoToForgottenPasswordPressed() {
        if (mListener != null) {
            mListener.onGoToForgottenPasswordInteraction();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginInteractionListener) {
            mListener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginInteractionListener {
        void onLoginInteraction(String password);

        void onGoToRegisterInteraction();

        void onGoToForgottenPasswordInteraction();
    }
}
