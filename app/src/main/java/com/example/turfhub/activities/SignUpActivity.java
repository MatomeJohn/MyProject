package com.example.turfhub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turfhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mConfirmPassword;
    Button mRegisterButton;
    TextView mLoginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFullName = findViewById(R.id.inputName);
        mEmail = findViewById(R.id.inputEmail);
        mPassword = findViewById(R.id.inputPassword);
        mConfirmPassword = findViewById(R.id.inputConfirmPassword);
        mRegisterButton = findViewById(R.id.buttonSignUp);
        mLoginButton = findViewById(R.id.textSignIn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterButton.setOnClickListener(v -> {
            String emailPatterns = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String[] allowedEmailDomains = {"outlook.com", "yahoo.com", "gmail.com"};
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String name = mFullName.getText().toString().trim();
            String confirmPassword = mConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)){
                mFullName.setError("Name is Required");
                return;
            }
            if (TextUtils.isEmpty(password)){

                mPassword.setError("Password is required");
                return;
            }

            if (password.length()<6){

                mPassword.setError("password must be 6 or more characters");
                return;

            }
            if (TextUtils.isEmpty(confirmPassword)){
                mConfirmPassword.setError("Confirm password is Required");
                return;
            }
            else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)){

                mConfirmPassword.setError("Passwords do not match");
                return;
            }


            if (email.matches(emailPatterns)) {
                String[] emailParts = email.split("@");
                String domain = emailParts[1];
                if (Arrays.asList(allowedEmailDomains).contains(domain)) {
                    // Email address is valid and allowed


                } else {
                    // Email address is not allowed
                    mEmail.setError("Email is not valid");
                    return;
                }
            } else {
                // Email address is invalid
                mEmail.setError(" email is not valid");
                return;
            }
           /* if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is Required");
                return;
            }

            else if(email.isEmpty() || !email.contains("@")){

                mEmail.setError("Email is not valid");
                return;
            }*/



            progressBar.setVisibility(View.VISIBLE);

            // register the user in the firebase

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {


                if (task.isSuccessful()){


                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));


                }else {
                    Toast.makeText(SignUpActivity.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();


                }

            });

        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });



    }
}
