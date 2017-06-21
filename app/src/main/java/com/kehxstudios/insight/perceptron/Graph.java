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

package com.kehxstudios.insight.perceptron;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.GameControl;
import com.kehxstudios.insight.tools.Tools;

/**
 *
 */

public class Graph extends GameControl {

    private float learningRate;
    private float[] weights;
    private Point[] points;

    private Paint backgroundPaint, linePaint, pPaint, nPaint, cPaint, iPaint;

    private boolean currentlyTraining, newTrainingPool, trainingComplete;

    public Graph(GameActivity gameActivity, float width, float height) {
        super(gameActivity, width, height);
        learningRate = 0.1f;
        currentlyTraining = false;
        newTrainingPool = false;
        trainingComplete = false;
        weights = new float[3];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat() * 2 - 1;
        }
        newTestPoints();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (newTrainingPool) {
            newTestPoints();
            trainingComplete = false;
            newTrainingPool = false;
        }

        if (currentlyTraining) {
            int correct = 0;
            for (Point point : points) {
                float[] inputs = {point.x, point.y, 1};
                if (train(inputs, point.label)) {
                    point.correct = true;
                    correct++;
                } else {
                    point.correct = false;
                }
            }
            if (correct == points.length) {
                currentlyTraining = false;
                trainingComplete = true;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        float x1 = Tools.map(-1, -1, 1, 0, width);
        float y1 = Tools.map(line(-1), -1, 1, height, 0);
        float x2 = Tools.map(1, -1, 1, 0, width);
        float y2 = Tools.map(line(1), -1, 1, height, 0);

        canvas.drawLine(x1, y1, x2, y2, linePaint);

        float gx1 = Tools.map(-1, -1, 1, 0, width);
        float gy1 = Tools.map(guessY(-1), -1, 1, height, 0);
        float gx2 = Tools.map(1, -1, 1, 0, width);
        float gy2 = Tools.map(guessY(1), -1, 1, height, 0);

        canvas.drawLine(gx1, gy1, gx2, gy2, linePaint);

        if (points != null) {
            for (Point point : points) {
                float px = Tools.map(point.x, -1, 1, 0, width);
                float py = Tools.map(point.y, -1, 1, height, 0);

                if (point.label == 1)
                    canvas.drawCircle(px, py, 20f, pPaint);
                else
                    canvas.drawCircle(px, py, 20f, nPaint);
                if (point.correct)
                    canvas.drawCircle(px, py, 10f, cPaint);
                else
                    canvas.drawCircle(px, py, 10f, iPaint);
            }
        }
    }

    private void newTestPoints() {
        points = new Point[100];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1);
            if (points[i].y > line(points[i].x)) {
                points[i].label = 1;
            } else {
                points[i].label = -1;
            }
        }
    }

    private int guess(float[] inputs) {
        float sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        int output = sign(sum);
        return output;
    }

    private float guessY(float x) {
        return -(weights[2]/weights[1]) - (weights[0]/weights[1]) * x;
    }

    private boolean train(float[] inputs, int target) {
        int guess = guess(inputs);
        int error = target - guess;

        if (error == 0)
            return true;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * inputs[i] * learningRate;
        }
        return false;
    }

    private int sign(float n) {
        if (n >= 0)
            return 1;
        else
            return -1;
    }

    public float line(float x) {
        return 0.3f * x + 0.2f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (trainingComplete) {
            newTestPoints();
        }
        currentlyTraining = true;
        return false;
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);

        pPaint = new Paint();
        pPaint.setColor(Color.BLACK);
        pPaint.setStyle(Paint.Style.FILL);

        nPaint = new Paint();
        nPaint.setColor(Color.BLACK);
        nPaint.setStyle(Paint.Style.STROKE);
        nPaint.setStrokeWidth(5f);

        cPaint = new Paint();
        cPaint.setColor(Color.GREEN);
        cPaint.setStyle(Paint.Style.FILL);

        iPaint = new Paint();
        iPaint.setColor(Color.RED);
        iPaint.setStyle(Paint.Style.FILL);
    }
}
