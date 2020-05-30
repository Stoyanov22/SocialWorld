package com.socialworld.mobile.ui.authorize;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.socialworld.mobile.R;

public class RegisterFragment extends Fragment {

    private EditText password;
    private EditText passwordRepeat;
    private CheckBox termsCheckBox;

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

        password = view.findViewById(R.id.password);
        passwordRepeat = view.findViewById(R.id.password_repeat);
        termsCheckBox = view.findViewById(R.id.terms_check_box);

        view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passwordRepeat.getText().toString().equals(password.getText().toString())) {
                    Toast.makeText(requireContext(), R.string.passwords_mismatch, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!termsCheckBox.isChecked()) {
                    Toast.makeText(requireContext(), R.string.please_confirm_terms, Toast.LENGTH_LONG).show();
                    return;
                }

                onNewRegisterPressed(password.getText().toString());
            }
        });

        return view;
    }

    private void onNewRegisterPressed(String password) {
        if (mListener != null) {
            mListener.onNewRegisterInteraction(password);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
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
        void onNewRegisterInteraction(String password);
    }
}
