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
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;
import java.util.Map;

public class RegisterS extends AppCompatActivity {
   public static final String TAG = RegisterS.class.getName();
    EditText sFullname,sEmail,sPassword, sPhone;
    Button mSignupBtn;
    TextView mSignin;
    ProgressBar progressBar;
    String userId;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;


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
        sPhone = findViewById(R.id.studentPhoneR);


        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = sFullname.getText().toString().trim();
                final String email = sEmail.getText().toString().trim();
                final String password = sPassword.getText().toString().trim();
                final String phone = sPhone.getText().toString().trim();


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

                if (TextUtils.isEmpty(phone)){
                    sPhone.setError("Phone # Required !");
                    return;
                }

                if (password.length() <6){
                    sPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                if (name.length() <3){
                    sFullname.setError("Please Enter Full Name !");
                    return;
                }

                if (email.length() <=5){
                    sEmail.setError("Email must contain 6 characters");
                    return;
                }

                if (phone.length() <=10){
                    sPhone.setError("Please Enter Valid Phone #");
                    return;
                }

                if (TextUtils.isDigitsOnly(name)){
                    sFullname.setError("Please Enter Alphabet !");
                    return;
                }

                if (name.startsWith("1") || name.startsWith("2") || name.startsWith("3") || name.startsWith("4") || name.startsWith("5") ||
                name.startsWith("6") || name.startsWith("7") || name.startsWith("8") || name.startsWith("9") || name.startsWith("0")){
                    sFullname.setError("Alphabets Only !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                // Register User in Firebase...!!!

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterS.this, "User Created !", Toast.LENGTH_SHORT).show();

                            // FireStore !!!!
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Students").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Full Name", name);
                            user.put("Email", email);
                            user.put("Password", password);
                            user.put("Phone No.", phone);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user profile is created for" + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure:" + e.toString());
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), DashboardS.class));
                            sFullname.setText(null);
                            sEmail.setText(null);
                            sPassword.setText(null);
                            progressBar.setVisibility(View.GONE);
                            sPhone.setText(null);
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
