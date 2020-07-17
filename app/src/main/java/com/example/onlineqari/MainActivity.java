package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


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
        Intent intent = new Intent(this, teacherR.class);
        startActivity(intent);
    }
}
