package com.example.bookingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Profile_fragment extends Fragment {
    private TextView tvUsername, tvPhoneNo, tvEmail, edPassword;
    private ImageView profileImg;
    private Button btn_edProfile, btn_logout, btn_edit_username, btn_edit_phoneNo, btn_edit_email;
    private String userId;
    private int countProfile=0;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    StorageReference storageReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUsername = v.findViewById(R.id.tvUsername);
        tvPhoneNo = v.findViewById(R.id.tvPhoneNo);
        tvEmail = v.findViewById(R.id.tvEmail);
        btn_edit_username = v.findViewById(R.id.btnEditUsername);
        btn_edit_phoneNo = v.findViewById(R.id.btnEditPhoneNo);
        btn_edit_email = v.findViewById(R.id.btnEditEmail);
        edPassword = v.findViewById(R.id.tvChangePassword);
        profileImg = v.findViewById(R.id.profile_picture);

        //get current user
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        user = firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("user/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImg);
            }
        });

        //Get profile data
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.getString("Username");
                String phoneNo = documentSnapshot.getString("PhoneNo");
                String email = documentSnapshot.getString("Email");

                tvUsername.setText(userName);
                tvPhoneNo.setText(phoneNo);
                tvEmail.setText(email);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        btn_edProfile = v.findViewById(R.id.btn_edit_profile);
        btn_edProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countProfile = (countProfile+1) % 2;

                if(countProfile == 0){
                    //Username button
                    btn_edit_username.setVisibility(View.INVISIBLE);
                    btn_edit_username.setEnabled(false);

                    //Phone No button
                    btn_edit_phoneNo.setVisibility(View.INVISIBLE);
                    btn_edit_phoneNo.setEnabled(false);

                    //Email button
                    btn_edit_email.setVisibility(View.INVISIBLE);
                    btn_edit_email.setEnabled(false);
                }

                else{
                    //Username button
                    btn_edit_username.setVisibility(View.VISIBLE);
                    btn_edit_username.setEnabled(true);

                    //Phone No button
                    btn_edit_phoneNo.setVisibility(View.VISIBLE);
                    btn_edit_phoneNo.setEnabled(true);

                    //Email button
                    btn_edit_email.setVisibility(View.VISIBLE);
                    btn_edit_email.setEnabled(true);
                }
            }
        });

        btn_edit_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.profile_details_change, null);

                final TextView tvProfile = v.findViewById(R.id.textViewProfile);
                final EditText edUsername = v.findViewById(R.id.editTextProfile);
                final TextInputLayout textInputLayout= v.findViewById(R.id.textInputLayout1);
                Button btn_done = v.findViewById(R.id.button_done);

                textInputLayout.setHint("New Username");
                tvProfile.setText("Edit Username");

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v); //set view to dialog

                AlertDialog dialog = builder.create();
                dialog.show();

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = edUsername.getText().toString().trim();

                        if (TextUtils.isEmpty(username)) {
                            edUsername.setError("Enter your phone number");
                            edUsername.requestFocus();
                            return;
                        }

                        dialog.dismiss();
                        changeProfileDetails(username, "Username", tvUsername);
                    }
                });
            }
        });

        btn_edit_phoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.profile_details_change, null);

                final TextView tvProfile = v.findViewById(R.id.textViewProfile);
                final EditText edPhoneNo = v.findViewById(R.id.editTextProfile);
                final TextInputLayout textInputLayout= v.findViewById(R.id.textInputLayout1);
                Button btn_done = v.findViewById(R.id.button_done);

                textInputLayout.setHint("New Phone Number");
                tvProfile.setText("Edit Phone Number");

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v); //set view to dialog

                AlertDialog dialog = builder.create();
                dialog.show();

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = edPhoneNo.getText().toString().trim();

                        if (TextUtils.isEmpty(phone)) {
                            edPhoneNo.setError("Enter your phone number");
                            edPhoneNo.requestFocus();
                            return;
                        }

                        if (!Patterns.PHONE.matcher(phone).matches()){
                            edPhoneNo.setError("Invalid phone number");
                            edPhoneNo.requestFocus();
                            return;
                        }

                        dialog.dismiss();
                        changeProfileDetails(phone, "PhoneNo", tvPhoneNo);
                    }
                });
            }
        });

        btn_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = LayoutInflater.from(getActivity()).inflate(R.layout.profile_details_change, null);

                final TextView tvProfile = v.findViewById(R.id.textViewProfile);
                final EditText edEmail = v.findViewById(R.id.editTextProfile);
                final TextInputLayout textInputLayout= v.findViewById(R.id.textInputLayout1);
                Button btn_done = v.findViewById(R.id.button_done);

                textInputLayout.setHint("New Email");
                tvProfile.setText("Edit Email");

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(v); //set view to dialog

                AlertDialog dialog = builder.create();
                dialog.show();

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newEmail = edEmail.getText().toString().trim();

                        if (TextUtils.isEmpty(newEmail)) {
                            edEmail.setError("Enter a new email");
                            edEmail.requestFocus();
                            return;
                        }
                        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                            edEmail.setError("Invalid email format");
                            edEmail.requestFocus();
                            return;
                        }

                        dialog.dismiss();
                        changeEmail(newEmail);
                    }
                });
            }
        });

        edPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        btn_logout = v.findViewById(R.id.button_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//Log out

                Toast.makeText(getActivity(), "You are logged out", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        return  v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflate menu
        inflater.inflate(R.menu.history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item click
        int id = item.getItemId();

        if (id == R.id.history){
            Toast.makeText(getActivity(), "History", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), history.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeProfileDetails(String profileDetails, String details, TextView textView){
        DocumentReference documentReference1 = db.collection("Users").document(userId);
        documentReference1.update(details, profileDetails);

        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String profile = documentSnapshot.getString(details);

                textView.setText(profile);

                Toast.makeText(getActivity(), "Profile changed successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChangePasswordDialog(){
        //inflate layout for dialog
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.profile_password_change, null);
        final EditText currentPassword = v.findViewById(R.id.editTextCurrentPassword);
        final EditText newPassword = v.findViewById(R.id.editTextNewPassword);
        final EditText cfnPassword = v.findViewById(R.id.editTextCfnPassword);
        Button btn_done = v.findViewById(R.id.button_done);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v); //set view to dialog

        AlertDialog dialog = builder.create();
        dialog.show();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate the data
                String current_password = currentPassword.getText().toString().trim();
                String new_password = newPassword.getText().toString().trim();
                String cfn_password = cfnPassword.getText().toString().trim();

                if (TextUtils.isEmpty(current_password)) {
                    currentPassword.setError("Enter the current password");
                    currentPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(new_password)) {
                    newPassword.setError("Enter the new password");
                    newPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(cfn_password)) {
                    cfnPassword.setError("Enter the new password again");
                    cfnPassword.requestFocus();
                    return;
                }

                if (new_password.length() < 6) {
                    edPassword.setError("Password too short, must at least 6 character");
                    edPassword.requestFocus();
                    return;
                }

                if (!cfn_password.equals(new_password)) {
                    cfnPassword.setError("Password not matching");
                    cfnPassword.requestFocus();
                    return;
                }

                dialog.dismiss();
                changePassword(current_password, new_password);
            }
        });
    }

    private void changePassword(String currentPassword, final String newPassword){

        //before changing password re-authenticate the user
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //successfully authenticated, begin update and change
                user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //password updated
                        Toast.makeText(getActivity(), "Password change successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating password, show reason
                        Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //authenticated failed, show reason
                Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeEmail(String newEmail){
        user.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DocumentReference documentReference1 = db.collection("Users").document(userId);
                documentReference1.update("Email", newEmail);

                documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String e = documentSnapshot.getString("Email");

                        tvEmail.setText(e);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //email updated
                Toast.makeText(getActivity(), "Email change successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //failed updating email, show reason
                Toast.makeText(getActivity(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImg.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }
    }

    public void uploadImageToFirebase(Uri imageUri){
        //upload image to firebase storage
        final StorageReference fileRef = storageReference.child("user/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImg);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
