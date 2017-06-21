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

import android.util.Log;

/**
 * Created by ReidC on 2017-06-10.
 */

public class Node {

    public int value;
    public float x, y;
    public int count;
    public int branch;
    public Node left;
    public Node right;

    public Node(int value, float x, float y) {
        this.value = value;
        this.x = x;
        this.y = y;
        count = 1;
        left = null;
        right = null;
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
        Log.d("Point", value+"");
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
}
