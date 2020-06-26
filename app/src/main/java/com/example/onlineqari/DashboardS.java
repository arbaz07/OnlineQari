package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_s);
    }

    public void joinLiveClass(View view){
        Intent intent = new Intent(this, JoinLiveSession.class);
        startActivity(intent);
    }
}
