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

        boolean randomNodes = true;
        if (randomNodes) {
            tree.addValue(50);
            tree.addValue(25);
            tree.addValue(75);
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                tree.addValue(random.nextInt(100));
            }
        } else {
            tree.addValue(50);
            tree.addValue(25);
            tree.addValue(75);
            tree.addValue(15);
            tree.addValue(35);
            tree.addValue(65);
            tree.addValue(85);
            tree.addValue(5);
            tree.addValue(20);
            tree.addValue(95);
            tree.addValue(80);
            tree.addValue(30);
            tree.addValue(40);
            tree.addValue(60);
            tree.addValue(70);
            tree.addValue(2);
            tree.addValue(10);
            tree.addValue(22);
            tree.addValue(55);
            tree.addValue(62);
            tree.addValue(71);
            tree.addValue(94);
            tree.addValue(69);
            tree.addValue(19);
            tree.addValue(31);
            tree.addValue(42);
            tree.addValue(82);
            tree.addValue(28);
            tree.addValue(62);
            tree.addValue(39);
            tree.addValue(77);
        }
        tree.traverse();
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
