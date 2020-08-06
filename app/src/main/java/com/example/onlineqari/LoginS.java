package com.example.onlineqari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginS extends AppCompatActivity {
    EditText emailS,passwordS;
    Button signinS;
    ProgressBar progressBar;
    FirebaseAuth sAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_s);

        emailS = findViewById(R.id.studentEmailL);
        passwordS = findViewById(R.id.studentPasswordL);
        signinS = findViewById(R.id.signinBtnS);
        progressBar = findViewById(R.id.progressBarS);

        sAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

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

                sAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser){
                            Toast.makeText(LoginS.this,"Please Create Account ! You are not a authenticated user.",Toast.LENGTH_SHORT).show();
                            emailS.setText(null);
                            passwordS.setText(null);
                            progressBar.setVisibility(View.GONE);
                        }else {
                            Log.e("TAG", "User Of This App !");
                        }
                    }
                });


                final CollectionReference collectionReferenceT = fstore.collection("users");
                Query query = collectionReferenceT.whereEqualTo("Email", email);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String email = document.getString("Email");
                                String password = document.getString("Password");
                                sAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        collectionReferenceT.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot document = task.getResult();
                                                String type = document.getString("Type");
                                                if (task.isSuccessful() && type.equals("student")){
                                                    Toast.makeText(LoginS.this,"SignIn Successfully",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(LoginS.this,DashboardS.class));
                                                    emailS.setText(null);
                                                    passwordS.setText(null);
                                                    progressBar.setVisibility(View.GONE);
                                                }else if (type.equals("teacher")){
                                                    Toast.makeText(LoginS.this, "Login Invalid ! You are a teacher ", Toast.LENGTH_SHORT).show();
                                                    emailS.setText(null);
                                                    passwordS.setText(null);
                                                    progressBar.setVisibility(View.GONE);
                                                }else if (type.equals("admin")){
                                                    Toast.makeText(LoginS.this,"SignIn Successfully !",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginS.this, DashboardS.class));
                                                    emailS.setText(null);
                                                    passwordS.setText(null);
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

                // Authenticate Student ..!!




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
