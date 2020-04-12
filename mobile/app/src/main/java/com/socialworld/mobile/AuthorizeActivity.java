package com.socialworld.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    private RelativeLayout loadingLayout;
    private FirebaseFirestore db;

    private EditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        firebaseAuth = FirebaseAuth.getInstance();

        loadingLayout = findViewById(R.id.loading_layout);

        emailInput = findViewById(R.id.email);

        db = FirebaseFirestore.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.authorize_fragment, LoginFragment.newInstance()).commit();
    }

    @Override
    public void onLoginInteraction(String password) {
        if (emailInput.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "The password is too short", Toast.LENGTH_LONG).show();
            return;
        }
        loadingLayout.setVisibility(View.VISIBLE);
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
                        loadingLayout.setVisibility(View.INVISIBLE);
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
        if (emailInput.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "The password is too short", Toast.LENGTH_LONG).show();
            return;
        }
        loadingLayout.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserEntity user = new UserEntity(firebaseAuth.getCurrentUser().getUid(), emailInput.getText().toString());
                        db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingLayout.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingLayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onForgottenPasswordInteraction() {
        if (emailInput.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Email cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        loadingLayout.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(emailInput.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadingLayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.beginTransaction().replace(R.id.authorize_fragment, LoginFragment.newInstance()).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingLayout.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
