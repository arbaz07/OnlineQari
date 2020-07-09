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

public class teacherR extends AppCompatActivity {
    EditText tFullname,tEmail,tPassword;
    Button tSignupBtn;
    TextView tSignin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_r);

        tFullname = findViewById(R.id.teacherNameR);
        tEmail = findViewById(R.id.teacherEmailR);
        tPassword = findViewById(R.id.teacherPasswordR);
        tSignupBtn = findViewById(R.id.signupBtnT);
        tSignin = findViewById(R.id.alreadyMember);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();


        tSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tFullname.getText().toString().trim();
                String email = tEmail.getText().toString().trim();
                String password = tPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    tFullname.setError("Name Required !");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    tEmail.setError("Email Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    tPassword.setError("Password Required !");
                    return;
                }

                if (password.length() <6){
                    tPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Register User in Firebase...!!!

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(teacherR.this, "User Created !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashboardT.class));
                            tFullname.setText(null);
                            tEmail.setText(null);
                            tPassword.setText(null);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(teacherR.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
    }

    public void loginAlready(View view){
        Intent intent = new Intent(this,LoginT.class);
        startActivity(intent);
    }
}
