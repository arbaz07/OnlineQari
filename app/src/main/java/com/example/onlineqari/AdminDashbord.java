package com.example.onlineqari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashbord extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashbord);
    }

    public void sAddBtn(View view){
        Intent intent = new Intent(this, addS.class);
        startActivity(intent);
    }
    public void tAddBtn(View view){
        Intent intent = new Intent(this, addT.class);
        startActivity(intent);
    }
}
