package com.example.turfhub.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turfhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    TextView forgotTextLink;
    FirebaseAuth fAuth;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail = findViewById(R.id.inputEmail);
        mPassword = findViewById(R.id.inputPassword);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mLoginBtn = findViewById(R.id.buttonSignIn);
        mCreateBtn = findViewById(R.id.textCreateNewAccount);
        forgotTextLink = findViewById(R.id.forgotPassword);

        mLoginBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)){

                mPassword.setError("Password is required");
                return;
            }

            if (password.length()<6){

                mPassword.setError("pass must be 6 or more characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //Authenticate the user
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

               if (task.isSuccessful()){

                   Toast.makeText(SignInActivity.this, "Signed In successfully", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));

               }else {
                   Toast.makeText(SignInActivity.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();

               }

            });

        });
        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)
        ));

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Extract the email and send send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(SignInActivity.this, "Reset Link sent To your Email.", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInActivity.this, "Error ! Reset Link is not valid" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog

                    }
                });
                passwordResetDialog.create().show();



            }
        });

    }
}