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

package com.kehxstudios.insight.terrainGeneration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.kehxstudios.insight.tools.GameActivity;

/**
 * Created by ReidC on 2017-06-18.
 */

public class TerrainGenerationActivity extends GameActivity {

    private int cols, rows;
    private float scl;
    private Paint backgroundPaint, linePaint;

    @Override
    protected void init() {
        scl = (int)(width/25);
        cols = (int)(width/scl);
        rows = (int)(height/scl);
    }

    @Override
    protected void start() {}

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (int y = 0; y < rows; y++) {
            Path path = new Path();

            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(0, y * scl);
            for (int x = 0; x < cols; x++) {
                path.lineTo(x*scl, y * scl);
                path.lineTo(x*scl, (y + 1) * scl);
                //canvas.drawRect(x*scl, y*scl, x*scl + scl, y*scl + scl, linePaint);
            }
            path.close();
            canvas.drawPath(path, linePaint);
        }
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);

    }

}
