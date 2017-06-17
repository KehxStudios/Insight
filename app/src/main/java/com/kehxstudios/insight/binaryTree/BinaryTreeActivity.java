package com.kehxstudios.insight.binaryTree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.kehxstudios.insight.tools.ViewPanel;

import java.util.Random;

public class BinaryTreeActivity extends AppCompatActivity {

    public ViewPanel view;
    public Tree tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        view = new ViewPanel(this);
        setContentView(view);

        generateTree();
    }

    private void generateTree() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        tree = new Tree(view, metrics.widthPixels, metrics.heightPixels);
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
