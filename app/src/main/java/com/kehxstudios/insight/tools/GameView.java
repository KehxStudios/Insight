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

package com.kehxstudios.insight.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-11.
 */

public class GameView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private boolean running = false;
    private float updatesPerSecond;

    private ArrayList<GameObject> gameObjects;

    public GameView(Context context) {
        super(context);
        updatesPerSecond = 30f;
        surfaceHolder = getHolder();
        gameObjects = new ArrayList<>();
    }

    public void setUpdatesPerSecond(float ups) {
        if (ups >= 1f && ups < 1000f) {
            updatesPerSecond = ups;
        } else {
            Log.e("GameView", "setting @updatesPerSecond to out or range value, " + ups);
        }
    }

    public void update(float delta) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(delta);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawARGB(255, 255, 255, 255);
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(canvas);
        }

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
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000 / updatesPerSecond;
        float delta = 0;
        int frames = 0;
        int updates = 0;
        while (running) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update(1/updatesPerSecond);
                updates++;
                delta--;
            }
            Canvas canvas = surfaceHolder.lockCanvas();
            draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
            frames++;

            if (System.currentTimeMillis() - timer >= 1000) {
                Log.d("GameView", "ups - " + updates +" | fps - " + frames);
                timer += 1000;
                frames = 0;
                updates = 0;
            }
        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }

    public void resume() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }
}
