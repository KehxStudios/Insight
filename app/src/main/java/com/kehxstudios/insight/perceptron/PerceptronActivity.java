package com.kehxstudios.insight.perceptron;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.Tools;

/**
 * Created by ReidC on 2017-06-19.
 */

public class PerceptronActivity extends GameActivity {

    private float learningRate;
    private float[] weights;
    private Node[] nodes;

    private Paint backgroundPaint, linePaint, pPaint, nPaint, cPaint, iPaint;

    private boolean training;
    private int trainingIndex;

    @Override
    protected void init() {
        gameView.setUpdatesPerSecond(1f);
        learningRate = 0.1f;
        training = false;
        trainingIndex = 0;
        weights = new float[3];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat() * 2 - 1;
        }

        nodes = new Node[100];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1);
            if (nodes[i].y > line(nodes[i].x)) {
                nodes[i].label = 1;
            } else {
                nodes[i].label = -1;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        training = true;
        return false;
    }

    @Override
    protected void start() {

    }

    @Override
    public void update(float delta) {
        if (training) {
            int correct = 0;
            for (Node node : nodes) {
                float[] inputs = {node.x, node.y, 1};
                if (train(inputs, node.label)) {
                    node.correct = true;
                    correct++;
                } else {
                    node.correct = false;
                }
            }
            Log.d("Perceptron", "Correct - " + correct + " / " + nodes.length);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        float x1 = Tools.map(-1, -1, 1, 0, width);
        float y1 = Tools.map(line(-1), -1, 1, width, 0);
        float x2 = Tools.map(1, -1, 1, 0, width);
        float y2 = Tools.map(line(1), -1, 1, width, 0);

        canvas.drawLine(x1, y1, x2, y2, linePaint);

        float gx1 = Tools.map(-1, -1, 1, 0, width);
        float gy1 = Tools.map(guessY(-1), -1, 1, width, 0);
        float gx2 = Tools.map(1, -1, 1, 0, width);
        float gy2 = Tools.map(guessY(1), -1, 1, width, 0);

        canvas.drawLine(gx1, gy1, gx2, gy2, linePaint);

        for (Node node : nodes) {
            float px = Tools.map(node.x, -1, 1, 0, width);
            float py = Tools.map(node.y, -1, 1, width, 0);

            if (node.label == 1)
                canvas.drawCircle(px, py, 20f, pPaint);
            else
                canvas.drawCircle(px, py, 20f, nPaint);
            if (node.correct)
                canvas.drawCircle(px, py, 10f, cPaint);
            else
                canvas.drawCircle(px, py, 10f, iPaint);
        }
    }

    public float line(float x) {
        return 0.3f * x + 0.2f;
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
