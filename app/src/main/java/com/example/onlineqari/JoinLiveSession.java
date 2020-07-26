package com.example.onlineqari;

import androidx.appcompat.app.AppCompatActivity;

import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.graphics.Point;
import android.view.Display;

import android.widget.MediaController;
import android.view.MotionEvent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class JoinLiveSession extends AppCompatActivity {

    private static final String APPLICATION_ID = "ep38gXubdzE95Wf6mnEZ3A";
    private static final String API_KEY = "Jg79Yu7YNA5nmCoygpSEis";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_live_session);

        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);
        mPlayerContentView = findViewById(R.id.PlayerContentView);

        mDefaultDisplay = getWindowManager().getDefaultDisplay();  //Sizing the video surface
    }

    SurfaceViewWithAutoAR mVideoSurface;
    TextView mPlayerStatusTextView;
    View mPlayerContentView;
    BroadcastPlayer mBroadcastPlayer;
    Display mDefaultDisplay;
    MediaController mMediaController = null;


    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {

            // FOR PLAYBACK..!!
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Status: " + playerState);

            //Adding player controls
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(JoinLiveSession.this);
                    mMediaController.setAnchorView(mPlayerContentView);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
                if (mMediaController != null) {
                    mMediaController.setEnabled(true);
                    mMediaController.show();
                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }
        }

        //Sizing The Video Surface..!!
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);
        }
    };


    // Get resourceUri for latest broadcast..!!
    @Override
    protected void onPause() {

        //Get resourceUri for latest broadcast
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;

        //Initiate a BroadcastPlayer
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;

        //Adding player controls
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }

    //Get resourceUri for latest broadcast
    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading latest broadcast");
        getLatestResourceUri();
    }

    void getLatestResourceUri() {
        Request request = new Request.Builder()
                .url("https://api.bambuser.com/broadcasts")
                .addHeader("Accept", "application/vnd.bambuser.v1+json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .get()
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() { @Override public void run() {
                    if (mPlayerStatusTextView != null)
                        mPlayerStatusTextView.setText("Http exception: " + e);
                }});
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String body = response.body().string();
                String resourceUri = null;
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray results = json.getJSONArray("results");
                    JSONObject latestBroadcast = results.optJSONObject(0);
                    resourceUri = latestBroadcast.optString("resourceUri");
                } catch (Exception ignored) {}
                final String uri = resourceUri;
                runOnUiThread(new Runnable() { @Override public void run() {
                    initPlayer(uri);
                }});
            }
        });
    }

    void initPlayer(String resourceUri) {

        //Get resourceUri for latest broadcast
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get info about latest broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }

        //Initiate a BroadcastPlayer
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();
    }

    final OkHttpClient mOkHttpClient = new OkHttpClient();


    // Sizing The Video Surface..!!
    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }

    // Adding player controls
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_UP && mBroadcastPlayer != null && mMediaController != null) {
            PlayerState state = mBroadcastPlayer.getState();
            if (state == PlayerState.PLAYING ||
                    state == PlayerState.BUFFERING ||
                    state == PlayerState.PAUSED ||
                    state == PlayerState.COMPLETED) {
                if (mMediaController.isShowing())
                    mMediaController.hide();
                else
                    mMediaController.show();
            } else {
                mMediaController.hide();
            }
        }
        return false;
    }

}
