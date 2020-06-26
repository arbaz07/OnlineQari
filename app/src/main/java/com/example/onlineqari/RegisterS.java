package com.example.onlineqari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterS extends AppCompatActivity {
    EditText sFullname,sEmail,sPassword;
    Button mSignupBtn;
    TextView mSignin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_s);

        sFullname = findViewById(R.id.studentNameR);
        sEmail = findViewById(R.id.studentEmailR);
        sPassword = findViewById(R.id.studentPasswordR);
        mSignupBtn = findViewById(R.id.signupBtnS);
        mSignin = findViewById(R.id.alreadyMember);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();


        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sFullname.getText().toString().trim();
                String email = sEmail.getText().toString().trim();
                String password = sPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    sFullname.setError("Name Required !");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    sEmail.setError("Email Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    sPassword.setError("Password Required !");
                    return;
                }

                if (password.length() <6){
                    sPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Register User in Firebase...!!!

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterS.this, "User Created !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashboardS.class));
                            sFullname.setText(null);
                            sEmail.setText(null);
                            sPassword.setText(null);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(RegisterS.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });


    }

    public void loginAlready(View view){
        Intent intent = new Intent(this,LoginS.class);
        startActivity(intent);
    }
}
