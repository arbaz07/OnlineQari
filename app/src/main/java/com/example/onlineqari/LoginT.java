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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginT extends AppCompatActivity {
    EditText emailT,passwordT;
    Button signinT;
    ProgressBar progressBar;
    FirebaseAuth tAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_t);

        emailT = findViewById(R.id.teacherEmail);
        passwordT = findViewById(R.id.teacherPassword);
        signinT = findViewById(R.id.signinBtnT);
        progressBar = findViewById(R.id.progressBarT);

        tAuth = FirebaseAuth.getInstance();

        signinT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailT.getText().toString().trim();
                String password = passwordT.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailT.setError("Email Must Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    passwordT.setError("Password Required !");
                    return;
                }

                if (password.length() <6){
                    passwordT.setError("Password Must be >= 6 Characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);



                // Authenticate Teacher

                tAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginT.this, "Logged In Successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashboardT.class));
                            emailT.setText(null);
                            passwordT.setText(null);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(LoginT.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
    }

    public void signupT(View view){
        Intent intent = new Intent(this, teacherR.class);
        startActivity(intent);
    }

}
