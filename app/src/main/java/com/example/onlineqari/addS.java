package com.example.onlineqari;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class addS extends AppCompatActivity {

    public static final String TAG = addS.class.getName();
    EditText aFullname, aEmail, aPassword, aPhone;
    Button aSignupBtn;
    ProgressBar progressBar;
    FirebaseAuth aAuth;
    FirebaseFirestore astore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_s);

        aFullname = findViewById(R.id.studentNameA);
        aEmail = findViewById(R.id.studentEmailA);
        aPassword = findViewById(R.id.studentPasswordA);
        aSignupBtn = findViewById(R.id.signupBtnA);
        progressBar = findViewById(R.id.progressBar);
        aPhone = findViewById(R.id.studentPhoneA);

        aAuth = FirebaseAuth.getInstance();
        astore = FirebaseFirestore.getInstance();

        aSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = aFullname.getText().toString().trim();
                final String email = aEmail.getText().toString().trim();
                final String password = aPassword.getText().toString().trim();
                final String phone = aPhone.getText().toString().trim();
                final String type = "student";

                if (TextUtils.isEmpty(name)){
                    aFullname.setError("Name Required !");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    aEmail.setError("Email Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    aPassword.setError("Password Required !");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    aPhone.setError("Phone # Required !");
                    return;
                }

                if (password.length() <6){
                    aPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }

                if (name.length() <3){
                    aFullname.setError("Please Enter Full Name !");
                    return;
                }

                if (email.length() <=5){
                    aEmail.setError("Email must contain 6 characters");
                    return;
                }

                if (phone.length() <=10){
                    aPhone.setError("Please Enter Valid Phone #");
                    return;
                }

                if (TextUtils.isDigitsOnly(name)){
                    aFullname.setError("Please Enter Alphabet !");
                    return;
                }


                if (name.startsWith("1") || name.startsWith("2") || name.startsWith("3") || name.startsWith("4") || name.startsWith("5") ||
                        name.startsWith("6") || name.startsWith("7") || name.startsWith("8") || name.startsWith("9") || name.startsWith("0")){
                    aFullname.setError("Alphabets Only !");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                // Register User in Firebase...!!!

                aAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(addS.this, "Student Create Successfully !", Toast.LENGTH_SHORT).show();

                            // FireStore !!!!
                            userId = aAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = astore.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Full Name", name);
                            user.put("Email", email);
                            user.put("Phone #", phone);
                            user.put("Password", password);
                            user.put("Type", type);

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
                            Toast.makeText(addS.this,"Student Create Successfully !",Toast.LENGTH_SHORT).show();
                            aFullname.setText(null);
                            aEmail.setText(null);
                            aPassword.setText(null);
                            aPhone.setText(null);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(addS.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            aFullname.setText(null);
                            aEmail.setText(null);
                            aPassword.setText(null);
                            aPhone.setText(null);
                        }

                    }
                });
            }
        });
    }

}
