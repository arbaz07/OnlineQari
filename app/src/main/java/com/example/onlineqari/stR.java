package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.onlineqari.R;

public class stR extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_r);
    }

    public void studentBtn(View view){
        Intent intent = new Intent(this,RegisterS.class);
        startActivity(intent);
    }

    public void teacherBtn(View view){
        Intent intent = new Intent(this, teacherR.class);
        startActivity(intent);
    }
}
