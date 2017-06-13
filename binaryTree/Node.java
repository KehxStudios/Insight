package com.kehxstudios.insight.binaryTree;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.kehxstudios.insight.GameObject;
import com.kehxstudios.insight.core.ViewPanel;

/**
 * Created by ReidC on 2017-06-10.
 */

public class Node implements GameObject {

    public int value;
    public float x, y;
    public int count;
    public int branch;
    public Node left;
    public Node right;

    private Paint paint, textPaint, branchPaint;

    public Node(int value, float x, float y) {
        this.value = value;
        this.x = x;
        this.y = y;
        count = 1;
        left = null;
        right = null;

        paint = new Paint();
        paint.setColor(Color.rgb(30,91,19));
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80f);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        branchPaint = new Paint();
        branchPaint.setColor(Color.rgb(139,69,19));
        branchPaint.setStyle(Paint.Style.STROKE);
        branchPaint.setStrokeWidth(20f);
    }

    public void addNode(Node node) {
        if (value > node.value) {
            if (left == null) {
                left = node;
                node.branch = branch + 1;
                node.x = x - node.x / (float)Math.pow(2, node.branch) / 2;
                node.y = y + 175;
            } else {
                left.addNode(node);
            }
        } else if (value < node.value){
            if (right == null) {
                right = node;
                node.branch = branch + 1;
                node.x = x + node.x / (float)Math.pow(2, node.branch) / 2;
                node.y = y + 175;
            } else {
                right.addNode(node);
            }
        } else {
            node.branch = branch;
            node.x = x;
            node.y = y;
            count++;
        }
    }

    public void visit() {
        if (left != null) {
            left.visit();
        }
        Log.d("Node", value+"");
        if (right != null) {
            right.visit();
        }
    }

    public Node search(int value) {
        if (this.value == value ) {
            return this;
        } else if (value < this.value && left != null) {
            return left.search(value);
        } else if (right != null) {
            return right.search(value);
        } else {
            return null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (left != null) {
            canvas.drawLine(x, y, left.x, left.y, branchPaint);
        }
        if (right != null) {
            canvas.drawLine(x, y, right.x, right.y, branchPaint);
        }
        canvas.drawCircle(x, y, 70, paint);
        canvas.drawText(value+"", x, y+30, textPaint);
    }

    @Override
    public void update() {

    }
}
