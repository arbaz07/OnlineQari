package com.example.onlineqari;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class delU extends AppCompatActivity {

    FirebaseAuth dAuth;
    FirebaseFirestore dStore;
    EditText userId;
    Button delB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_u);

        userId = findViewById(R.id.emailU);
        delB = findViewById(R.id.delBtn);

        dAuth = FirebaseAuth.getInstance();
        dStore = FirebaseFirestore.getInstance();


        delB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = userId.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    userId.setError("Email Required !");
                    return;
                }

                dAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser){
                            Toast.makeText(delU.this,"User Not Exist In Database !",Toast.LENGTH_SHORT).show();
                            userId.setText(null);
                        }else {
                          //  Log.e("TAG", "User Of This App !");

                            /*
                            String uid = dAuth.getUid();

                            dStore.collection("users").document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(delU.this,"Delete !",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(delU.this,"Error !",Toast.LENGTH_SHORT).show();
                                }
                            });*/
                        }
                    }
                });
            }
        });

    }
}
