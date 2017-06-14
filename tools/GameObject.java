package com.kehxstudios.insight.tools;

import android.graphics.Canvas;

/**
 * Created by ReidC on 2017-06-11.
 */

public interface GameObject {

    public void draw(Canvas canvas);
    public void update(float delta);
}
