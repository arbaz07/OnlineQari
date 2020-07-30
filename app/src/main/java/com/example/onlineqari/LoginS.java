package com.example.onlineqari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginS extends AppCompatActivity {
    EditText emailS,passwordS;
    Button signinS;
    ProgressBar progressBar;
    FirebaseAuth sAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_s);

        emailS = findViewById(R.id.studentEmailL);
        passwordS = findViewById(R.id.studentPasswordL);
        signinS = findViewById(R.id.signinBtnS);
        progressBar = findViewById(R.id.progressBarS);

        sAuth = FirebaseAuth.getInstance();

        signinS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailS.getText().toString().trim();
                String password = passwordS.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailS.setError("Email Must Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    passwordS.setError("Password Required !");
                    return;
                }

                if (password.length() <6){
                    passwordS.setError("Password Must be >= 6 Characters.");
                    return;
                }

                if (email.length() <=5){
                    emailS.setError("Email must contain 6 characters !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate Student ..!!

                sAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginS.this, "Logged In Successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashboardS.class));
                            emailS.setText(null);
                            passwordS.setText(null);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(LoginS.this, "Error Occur !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
    }

    public void siguupHere(View view){
        Intent intent = new Intent(this, RegisterS.class);
        startActivity(intent);
    }

    public void forgotPasswordS(View view){
        final EditText resetMail = new EditText(view.getContext());
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());

        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter Your Mail to Received Reset Link !");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Extract mail and Reset Link

                String mail = resetMail.getText().toString();

                if (TextUtils.isEmpty(mail)){
                    resetMail.setError("Please Enter Mail !");
                    return;
                }

                sAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginS.this,"Reset Link Send Successfully !", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginS.this,"Error Occur ! Link Is Not Send" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close the Dialog !
            }
        });

        passwordResetDialog.create().show();
    }

}
