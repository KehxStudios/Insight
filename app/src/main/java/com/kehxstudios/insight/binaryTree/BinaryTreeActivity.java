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

package com.kehxstudios.insight.binaryTree;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.kehxstudios.insight.tools.GameActivity;

import java.util.ArrayList;

public class BinaryTreeActivity extends GameActivity {

    public ArrayList<Node> nodes;
    public Node root;

    private Paint nodePaint, textPaint, branchPaint;

    @Override
    protected void init() {
        gameView.setUpdatesPerSecond(1f);

        nodes = new ArrayList<>();
        root = null;

        addValue(50);
        addValue(75);
        addValue(25);
    }

    @Override
    protected void start() {

    }

    public void traverse() {
        if (root != null) {
            root.visit();
        }
    }

    public Node search(int value) {
        if (root != null) {
            return root.search(value);
        } else {
            return null;
        }
    }

    @Override
    public void update(float delta) {
        addValue(random.nextInt(100));
    }


    private void addValue(int value) {
        if (root == null) {
            root = new Node(value, width/2, height/8);
            root.branch = 0;
            nodes.add(root);
        } else {
            Node node = new Node(value, width, height);
            root.addNode(node);
            nodes.add(node);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        for (Node node : nodes) {
            if (node.left != null) {
                canvas.drawLine(node.x, node.y, node.left.x, node.left.y, branchPaint);
            }
            if (node.right != null) {
                canvas.drawLine(node.x, node.y, node.right.x, node.right.y, branchPaint);
            }
            canvas.drawCircle(node.x, node.y, 70, nodePaint);
            canvas.drawText(node.value+"", node.x, node.y+30, textPaint);
        }
    }

    @Override
    protected void createPaints() {
        nodePaint = new Paint();
        nodePaint.setColor(Color.GREEN);
        nodePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(60f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        branchPaint = new Paint();
        branchPaint.setColor(Color.BLACK);
        branchPaint.setStyle(Paint.Style.STROKE);
        branchPaint.setStrokeWidth(20f);
    }
}
