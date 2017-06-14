package com.kehxstudios.insight.core;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.kehxstudios.insight.tools.GameObject;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-11.
 */

public class ViewPanel extends View implements Runnable, SurfaceHolder.Callback {

    private ArrayList<GameObject> gameObjects;

    public ViewPanel(Context context) {
        super(context);
        Log.d("ViewPanel", "Created");

        gameObjects = new ArrayList<>();
    }

    public void update() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("ViewPanel", "onDraw");
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(canvas);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d("ViewPanel", "draw");
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void addGameObject(GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
        }
    }

    @Override
    public void run() {




    }



}
