package com.kehxstudios.insight.starfield;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.Vector2;

import java.util.Random;

/**
 * Created by ReidC on 2017-06-18.
 */

public class Star {

    public Vector2 position;
    public float width, height;
    public float depth,previousDepth;

    public Star(float x, float y, float startDepth) {
        position = new Vector2(x,y);
        depth = startDepth;
        previousDepth = startDepth;
    }

    public void reset(float x, float y, float startDepth) {
        position.set(x,y);
        depth = startDepth;
        previousDepth = startDepth;
    }
}
