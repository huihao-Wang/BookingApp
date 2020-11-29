package com.example.bookingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private String name, phone, email, gender, password, cfnPassword, userID;
    private TextView tvGender, tvSignIn;
    private EditText edEmail, edPassword, edCfnPassword, edUsername, edPhoneNo;
    private Button btn_signUp;
    private RadioButton rbMale, rbFemale;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGender = findViewById(R.id.textViewGender);
        tvSignIn = findViewById(R.id.textViewSignIn);
        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        edCfnPassword = findViewById(R.id.editTextConfirmPassword);
        edUsername = findViewById(R.id.editTextUserName);
        edPhoneNo = findViewById(R.id.editTextPhone);
        rbMale = findViewById(R.id.radioButtonMale);
        rbFemale = findViewById(R.id.radioButtonFemale);
        progressBar = findViewById(R.id.progressBar_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Keep user login, if no logout account from the system
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainPage.class));
            finish();
        }

        btn_signUp = findViewById(R.id.sign_up_btn);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get All the user value
                name = edUsername.getText().toString().trim();
                phone = edPhoneNo.getText().toString().trim();
                email = edEmail.getText().toString().trim();
                password = edPassword.getText().toString().trim();
                cfnPassword = edCfnPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name) || name == null) {
                    edUsername.setError("Enter a name");
                    edUsername.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    edEmail.setError("Enter a email");
                    edEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edEmail.setError("Invalid email format");
                    edEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(phone) || phone == null) {
                    edPhoneNo.setError("Enter your phone number");
                    edPhoneNo.requestFocus();
                    return;
                }

                if (!Patterns.PHONE.matcher(phone).matches()){
                    edPhoneNo.setError("Invalid phone number");
                    edPhoneNo.requestFocus();
                    return;
                }

                if (!rbMale.isChecked() && !rbFemale.isChecked()){
                    tvGender.setError("Select gender");
                    tvGender.requestFocus();
                    return;
                }

                else{
                    tvGender.setError(null);
                }

                if (TextUtils.isEmpty(password)) {
                    edPassword.setError("Enter the password");
                    edPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(cfnPassword)) {
                    edCfnPassword.setError("Enter the password again");
                    edCfnPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    edPassword.setError("Password too short, must at least 6 character");
                    edPassword.requestFocus();
                    return;
                }

                if (!cfnPassword.equals(password)) {
                    edCfnPassword.setError("Password not matching");
                    edCfnPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()){

                                    //Add new document with a generate ID
                                    userID = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("Users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Username", name);
                                    user.put("Email", email);
                                    user.put("PhoneNo", phone);
                                    user.put("Gender", gender);
                                    user.put("History", "0");
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: user profile is created for" + userID);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: " + e.toString());
                                        }
                                    });

                                    Toast.makeText(MainActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(MainActivity.this, Login.class));

                                } else {
                                    Toast.makeText(MainActivity.this, "Register Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    public void radioButtonMale_onClick(View view) {
        rbFemale.setChecked(false);
        gender = "Male";
    }

    public void radioButtonFemale_onClick(View view) {
        rbMale.setChecked(false);
        gender = "Female";
    }

    public void tvSignIn_onCLick(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

}