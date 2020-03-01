package com.socialworld.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socialworld.mobile.entities.UserEntity;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText passwordRepeat;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onBtnRegisterClick(View v) {
        if (password.getText().toString().length() < 6) {
            Toast.makeText(RegisterActivity.this, "The password is too short", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordRepeat.getText().toString().equals(password.getText().toString())) {
            firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        db = FirebaseFirestore.getInstance();
                        UserEntity user = new UserEntity(username.getText().toString());
                        db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Intent categoryPage = new Intent(RegisterActivity.this, MainActivity.class);
                                finish();
                                Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_LONG).show();
                                startActivity(categoryPage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Couldn't activity_register the user", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_LONG).show();
        }
    }
}
