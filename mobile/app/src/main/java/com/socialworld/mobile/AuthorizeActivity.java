package com.socialworld.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socialworld.mobile.entities.UserEntity;
import com.socialworld.mobile.ui.authorize.ForgotPasswordFragment;
import com.socialworld.mobile.ui.authorize.LoginFragment;
import com.socialworld.mobile.ui.authorize.RegisterFragment;

public class AuthorizeActivity extends AppCompatActivity implements LoginFragment.OnLoginInteractionListener, RegisterFragment.OnNewRegisterInteractionListener, ForgotPasswordFragment.OnForgottenPasswordInteractionListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private AlertDialog loadingDialog;

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        firebaseAuth = FirebaseAuth.getInstance();

        AlertDialog.Builder builderLoading = new AlertDialog.Builder(AuthorizeActivity.this);
        builderLoading.setCancelable(false);
        builderLoading.setView(R.layout.loading_dialog);
        loadingDialog = builderLoading.create();

        emailInput = findViewById(R.id.email);

        db = FirebaseFirestore.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.authorize_fragment, LoginFragment.newInstance()).commit();
    }

    @Override
    public void onLoginInteraction(String password) {
        if (!isEmailValid()) {
            return;
        }
        if (!isPasswordValid(password)) {
            return;
        }
        showLoading();
        firebaseAuth.signInWithEmailAndPassword(emailInput.getText().toString(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onGoToRegisterInteraction() {
        getSupportFragmentManager().beginTransaction().replace(R.id.authorize_fragment, RegisterFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onGoToForgottenPasswordInteraction() {
        getSupportFragmentManager().beginTransaction().replace(R.id.authorize_fragment, ForgotPasswordFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onNewRegisterInteraction(String password) {
        if (!isEmailValid()) {
            return;
        }
        if (!isPasswordValid(password)) {
            return;
        }
        showLoading();
        firebaseAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserEntity user = new UserEntity(firebaseAuth.getCurrentUser().getUid(), emailInput.getText().toString());
                        db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), R.string.user_registered, Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        hideLoading();
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onForgottenPasswordInteraction() {
        if (!isEmailValid()) {
            return;
        }
        showLoading();
        firebaseAuth.sendPasswordResetEmail(emailInput.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), R.string.reset_pass_email_sent, Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.authorize_fragment, LoginFragment.newInstance()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showLoading() {
        loadingDialog.show();
    }

    private void hideLoading() {
        loadingDialog.hide();
    }

    private boolean isEmailValid() {
        if (emailInput.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.email_can_not_be_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.pass_can_not_be_empty, Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.pass_too_short, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
