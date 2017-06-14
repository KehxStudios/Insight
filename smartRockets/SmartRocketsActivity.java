package com.kehxstudios.insight.smartRockets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.kehxstudios.insight.core.ViewPanel;

/**
 * Created by ReidC on 2017-06-13.
 */

public class SmartRocketsActivity extends AppCompatActivity {

    public ViewPanel view;

    public Population population;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        view = new ViewPanel(this);
        setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        population = new Population(metrics.widthPixels, metrics.heightPixels);
    }





}