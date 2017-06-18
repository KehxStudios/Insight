package com.kehxstudios.insight.smartRockets;

import com.kehxstudios.insight.tools.Vector2;

/**
 * Created by ReidC on 2017-06-14.
 */

public class Barrier {

    public Vector2 position;
    public float width, height;

    public Barrier(float x, float y, float width, float height) {
        position = new Vector2(x,y);
        this.width = width;
        this.height = height;
    }

}
