package com.example.onlineqari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginT extends AppCompatActivity {
    EditText emailT,passwordT;
    Button signinT;
    ProgressBar progressBar;
    FirebaseAuth tAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_t);

        emailT = findViewById(R.id.teacherEmail);
        passwordT = findViewById(R.id.teacherPassword);
        signinT = findViewById(R.id.signinBtnT);
        progressBar = findViewById(R.id.progressBarT);
        final String type;

        tAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        signinT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailT.getText().toString().trim();
                final String password = passwordT.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailT.setError("Email Must Required !");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    passwordT.setError("Password Required !");
                    return;
                }

                if (password.length() <6){
                    passwordT.setError("Password Must be >= 6 Characters !");
                    return;
                }


                if (email.length() <=5){
                    emailT.setError("Email must contain 6 characters !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                final CollectionReference collectionReferenceT = fstore.collection("users");
                Query query = collectionReferenceT.whereEqualTo("Email", email);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("Email");
                                String password = document.getString("Password");
                                tAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        collectionReferenceT.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot document = task.getResult();
                                                String type = document.getString("Type");
                                                if (task.isSuccessful() && type.equals("teacher")){
                                                    startActivity(new Intent(LoginT.this,DashboardT.class));
                                                    emailT.setText(null);
                                                    passwordT.setText(null);
                                                    progressBar.setVisibility(View.GONE);
                                                }else {
                                                    Toast.makeText(LoginT.this, "Login Invalid ! You are not a Teacher", Toast.LENGTH_SHORT).show();
                                                    emailT.setText(null);
                                                    passwordT.setText(null);
                                                    progressBar.setVisibility(View.GONE);
                                                        }

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });


                // Authenticate Teacher




            }
        });
    }



    public void signupT(View view){
        Intent intent = new Intent(this, teacherR.class);
        startActivity(intent);
    }

    public void forgotPasswordT(View view){
        final EditText resetMail = new EditText(view.getContext());
        final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter Your Mail to Received Reset Link !");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // extract the mail and reset link

                String mail = resetMail.getText().toString();

                if (TextUtils.isEmpty(mail)){
                    resetMail.setError("Please Enter Mail !");
                    return;
                }

                tAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginT.this, "Reset Link Send To Mail !", Toast.LENGTH_SHORT ).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginT.this, "Error ! Link is not Send " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // close the dialog
            }
        });

        passwordResetDialog.create().show();
    }

}
