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

        final MediaPlayer mp ;
        mp = MediaPlayer.create(this,R.raw.a);

        ImageView im1 =  findViewById(R.id.img1);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        im1.startAnimation(animation);
        final Intent i = new Intent(this, stR.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    mp.start();

                    sleep(8000);
                    mp.release();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}




