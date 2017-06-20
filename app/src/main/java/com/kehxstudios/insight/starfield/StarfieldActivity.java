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

package com.kehxstudios.insight.starfield;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.WindowManager;

import com.kehxstudios.insight.tools.GameActivity;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-18.
 */

public class StarfieldActivity extends GameActivity {

    private ArrayList<Star> stars;
    private float starSpeed, starCount;
    private Paint backgroundPaint, starPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void init() {
        starSpeed = 40f;
        starCount = 1000f;
        stars = new ArrayList<>();
        for (int i = 0; i < starCount; i++) {
            stars.add(new Star(random.nextFloat() * width  - width/2, random.nextFloat() * height - height/2,
                    random.nextFloat() * height));
        }
    }

    @Override
    protected void start() {}

    @Override
    public void update(float delta) {
        for (Star star : stars) {
            star.previousDepth = star.depth;
            star.depth -= starSpeed;
            if (star.depth < 0) {
                star.reset(random.nextFloat() * width  - width/2, random.nextFloat() * height - height/2,
                        height);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        canvas.translate(width/2, height/2);
        for (Star star : stars) {
            float sx = star.position.x / star.depth * width;
            float sy = star.position.y / star.depth * height;
            float px = star.position.x / star.previousDepth * width;
            float py = star.position.y / star.previousDepth * height;

            canvas.drawLine(sx, sy, px, py, starPaint);
        }
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        starPaint = new Paint();
        starPaint.setColor(Color.WHITE);
        starPaint.setStyle(Paint.Style.STROKE);
        starPaint.setStrokeWidth(5f);
    }

}
