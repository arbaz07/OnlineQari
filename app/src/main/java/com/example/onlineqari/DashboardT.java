package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class DashboardT extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_t);
    }

    public void goLive(View view){
        Intent intent = new Intent(this, Golive.class);
        startActivity(intent);
    }
}
