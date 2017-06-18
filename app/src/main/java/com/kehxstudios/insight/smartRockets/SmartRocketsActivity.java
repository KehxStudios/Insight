package com.kehxstudios.insight.smartRockets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.kehxstudios.insight.tools.GameView;

/**
 * Created by ReidC on 2017-06-13.
 */

public class SmartRocketsActivity extends AppCompatActivity {

    public GameView view;

    public Population population;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        view = new GameView(this);
        setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        population = new Population(view, metrics.widthPixels, metrics.heightPixels);
    }


    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }
}