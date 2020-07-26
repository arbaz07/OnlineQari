package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;


public class Golive extends AppCompatActivity {

    private static final String APPLICATION_ID = "ep38gXubdzE95Wf6mnEZ3A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golive);

        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        mBroadcastButton = findViewById(R.id.BroadcastButton);
        liveText = findViewById(R.id.liveTextView);

        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);

        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());

        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting()){
                    mBroadcaster.startBroadcast();
                    liveText.setVisibility(View.VISIBLE);

                    // Send Notification..!!!
                    addNotification();
                }
                else{
                    mBroadcaster.stopBroadcast();
                    liveText.setVisibility(View.GONE);
                }

            }
        });
    }

    Button mBroadcastButton;
    SurfaceView mPreviewSurface;
    Broadcaster mBroadcaster;
    TextView liveText;


    private void addNotification(){
        // Notification Build..!!!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Live Class Notification !")
                .setContentText("Please Join Live Session Fast.");
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmsound);
        builder.setVibrate(new long[] {1000, 1000});


        // Create the intent
        Intent intent = new Intent(this, JoinLiveSession.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as Notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
 


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }


    // ANDROID App Permission ...!!!
    public void onResume() {

        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();

        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation()); // SCREEN Rotation AND KEEP AWAKE

        //Add required Android app permissions and features
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
    }
    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


    //Bootstrap the SDK..!!!
    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            if (broadcastStatus == BroadcastStatus.STARTING)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (broadcastStatus == BroadcastStatus.IDLE)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "Broadcast" : "Disconnect");

            Log.i("Mybroadcastingapp", "Received status change: " + broadcastStatus);
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("Mybroadcastingapp", "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {
        }
        @Override
        public void onChatMessage(String s) {
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
        }
        @Override
        public void onBroadcastIdAvailable(String s) {
        }
    };

}
