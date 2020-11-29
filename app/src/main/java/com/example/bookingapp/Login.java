package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText edLoginEmail, edLoginPassword;
    private Button btn_login;
    private ProgressBar progressBar;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edLoginEmail    = findViewById(R.id.editTextLoginEmail);
        edLoginPassword = findViewById(R.id.editTextLoginPassword);
        progressBar     = findViewById(R.id.progressBar_login);

        firebaseAuth = FirebaseAuth.getInstance();
        db           = FirebaseFirestore.getInstance();

        btn_login = findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edLoginEmail.getText().toString().trim();
                String password = edLoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edLoginEmail.setError("Enter your email");
                    edLoginEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edLoginPassword.setError("Enter the password");
                    edLoginPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    edLoginPassword.setError("Password too short, must at least 6 character");
                    edLoginPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //For authenticate the user
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Loading progress
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainPage.class));

                        } else {
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    public void tvSignUp_onCLick(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void forgetPassword_onClick(View view) {
        final EditText resetPassword_Email = new EditText(Login.this);

        AlertDialog.Builder forgetPasswordDialog = new AlertDialog.Builder(Login.this);
        forgetPasswordDialog.setTitle("Forget Password? ");
        forgetPasswordDialog.setMessage("Enter email to send reset password link");
        resetPassword_Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        forgetPasswordDialog.setView(resetPassword_Email);

        forgetPasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userEmail = resetPassword_Email.getText().toString().trim();

                if (userEmail.equals("")){
                    Toast.makeText(Login.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                }

                else{
                    sentPasswordToEmail(userEmail);
                }
            }
        });

        forgetPasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Close
            }
        });

        forgetPasswordDialog.create().show();
    }

    public void sentPasswordToEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Password reset email has sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}