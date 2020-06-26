package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void studentBtn(View view){
        Intent intent = new Intent(this,RegisterS.class);
        startActivity(intent);
    }

    public void teacherBtn(View view){
        Intent intent = new Intent(this, LoginT.class);
        startActivity(intent);
    }
}
