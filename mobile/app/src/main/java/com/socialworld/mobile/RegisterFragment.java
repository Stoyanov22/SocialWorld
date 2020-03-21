package com.socialworld.mobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText passwordRepeat;

    private OnNewRegisterInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment RegisterFragment.
     */
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        passwordRepeat = view.findViewById(R.id.password_repeat);

        view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().length() == 0 || password.getText().length() == 0){
                    Toast.makeText(getActivity().getApplicationContext(), "Email and password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.getText().toString().length() < 6) {
                    Toast.makeText(getActivity().getApplicationContext(), "The password is too short", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!passwordRepeat.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Passwords don't match!", Toast.LENGTH_LONG).show();
                    return;
                }
                onNewRegisterPressed(email.getText().toString(),password.getText().toString());
            }
        });

        return view;
    }

    public void onNewRegisterPressed(String email, String password) {
        if (mListener != null) {
            mListener.onNewRegisterInteraction(email, password);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewRegisterInteractionListener) {
            mListener = (OnNewRegisterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewRegisterInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnNewRegisterInteractionListener {
        void onNewRegisterInteraction(String email, String password);
    }
}
