package com.kehxstudios.insight.aStarPathfinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.kehxstudios.insight.tools.ViewPanel;

/**
 * Created by ReidC on 2017-06-17.
 */

public class AStarPathfindingActivity extends AppCompatActivity {

    public ViewPanel view;
    public Grid grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        view = new ViewPanel(this);
        setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        grid = new Grid(view, metrics.widthPixels, metrics.heightPixels);
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
