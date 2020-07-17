package com.example.onlineqari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class teacherR extends AppCompatActivity {
    public static final String TAG = teacherR.class.getName();
    EditText tFullname,tEmail,tPassword, tPhone, tEducation;
    Button tSignupBtn;
    TextView tSignin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;

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
        tPhone = findViewById(R.id.teacherPhoneR);
        tEducation = findViewById(R.id.teacherEducationR);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        tSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = tFullname.getText().toString().trim();
                final String email = tEmail.getText().toString().trim();
                final String password = tPassword.getText().toString().trim();
                final String phone = tPhone.getText().toString().trim();
                final String education = tEducation.getText().toString().trim();

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

                if (TextUtils.isEmpty(phone)){
                    tPhone.setError("Phone # Required !");
                    return;
                }

                if (TextUtils.isEmpty(education)){
                    tEducation.setError("Education Required !");
                    return;
                }

                if (password.length() <6){
                    tPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                if (name.length() <3){
                    tFullname.setError("Please Enter Full Name !");
                    return;
                }

                if (email.length() <=5){
                    tEmail.setError("Email must contain 6 characters");
                    return;
                }

                if (phone.length() <=10){
                    tPhone.setError("Please Enter Valid Phone #");
                    return;
                }

                if (education.length() <2){
                    tEducation.setError("Please Enter Valid Education");
                    return;
                }

                if (TextUtils.isDigitsOnly(name)){
                    tFullname.setError("Please Enter Alphabet !");
                    return;
                }

                if (TextUtils.isDigitsOnly(education)){
                    tEducation.setError("Alphabets Only !");
                    return;
                }

                if (name.startsWith("1") || name.startsWith("2") || name.startsWith("3") || name.startsWith("4") || name.startsWith("5") ||
                        name.startsWith("6") || name.startsWith("7") || name.startsWith("8") || name.startsWith("9") || name.startsWith("0")){
                    tFullname.setError("Alphabets Only !");
                    return;
                }

                if (education.startsWith("1") || education.startsWith("2") || education.startsWith("3") || education.startsWith("4") || education.startsWith("5") ||
                        education.startsWith("6") || education.startsWith("7") || education.startsWith("8") || education.startsWith("9") || education.startsWith("0")){
                    tEducation.setError("Alphabets Only ! !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Register User in Firebase...!!!

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(teacherR.this, "User Created !", Toast.LENGTH_SHORT).show();

                            // FireStore !!!!
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Full Name", name);
                            user.put("Email", email);
                            user.put("Phone #", phone);
                            user.put("Education", education);
                            user.put("Password", password);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for " + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure:" + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(), DashboardT.class));
                            tFullname.setText(null);
                            tEmail.setText(null);
                            tPassword.setText(null);
                            tEducation.setText(null);
                            tPhone.setText(null);
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
