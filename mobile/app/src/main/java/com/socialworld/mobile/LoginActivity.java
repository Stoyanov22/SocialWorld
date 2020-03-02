package com.socialworld.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    FirebaseAuth firebaseAuth;
    Fragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loadingFragment = new LoadingFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.progress_container, loadingFragment).hide(loadingFragment).commit();
    }

    public void onBtnLoginClick(View v) {
        if(username.getText().length() == 0 || password.getText().length() == 0){
            Toast.makeText(LoginActivity.this, "Email and password cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            getSupportFragmentManager().beginTransaction().show(loadingFragment).commit();
            firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(loadingFragment).commit();
                        Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void onBtnRegisterClick(View v) {
        Intent registerPage = new Intent(LoginActivity.this, RegisterActivity.class);
        finish();
        startActivity(registerPage);
    }
}
