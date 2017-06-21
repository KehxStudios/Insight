/*******************************************************************************
 * Copyright 2017 See AUTHORS file.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package com.kehxstudios.insight.intro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kehxstudios.insight.mainMenu.MainMenuActivity;
import com.kehxstudios.insight.R;

/**
 *
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
                            startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
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
