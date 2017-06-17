package com.kehxstudios.insight.evolutionarySteering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.kehxstudios.insight.tools.ViewPanel;

/**
 * Created by ReidC on 2017-06-17.
 */

public class EvolutionarySteeringActivity extends AppCompatActivity {

    public ViewPanel view;
    public Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        view = new ViewPanel(this);
        setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        map = new Map(view, metrics.widthPixels, metrics.heightPixels);
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
