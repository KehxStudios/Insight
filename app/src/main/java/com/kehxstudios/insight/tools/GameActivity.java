package com.kehxstudios.insight.tools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Random;

/**
 * Created by ReidC on 2017-06-18.
 */

public abstract class GameActivity extends AppCompatActivity implements GameObject {

    protected GameView gameView;
    protected float width, height;

    protected Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        gameView = new GameView(this);
        gameView.addGameObject(this);
        setContentView(gameView);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        random = new Random();

        createPaints();
        init();
    }

    protected abstract void init();
    protected abstract void start();
    protected abstract void createPaints();

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
