package com.kehxstudios.insight;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ReidC on 2017-06-11.
 */

public class IntroActivity extends AppCompatActivity {

    private MediaPlayer logoMusic;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_intro);

        logoMusic = MediaPlayer.create(IntroActivity.this, R.raw.forest);
        logoImage = (ImageView) findViewById(R.id.logo);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    if (hasWindowFocus()) {
                        logoImage.post(new Runnable() { public void run() { changeLogo(); }});
                        sleep(3000);
                        if (hasWindowFocus()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }

    private void changeLogo() {
        logoImage.setImageResource(R.drawable.app_logo);
        TextView name = (TextView) findViewById(R.id.appName);
        name.setVisibility(View.GONE);
        TextView version = (TextView) findViewById(R.id.appVersion);
        version.setVisibility(View.GONE);
    }

    protected void onStart() {
        super.onStart();
        logoMusic.start();
    }

    protected void onPause() {
        super.onPause();
        logoMusic.release();
    }

    protected void onResume() {
        super.onResume();
    }
}
