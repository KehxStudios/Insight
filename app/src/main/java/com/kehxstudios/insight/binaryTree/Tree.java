package com.kehxstudios.insight.binaryTree;

import android.graphics.Canvas;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.ViewPanel;

import java.util.Random;

/**
 * Created by ReidC on 2017-06-10.
 */

public class Tree implements GameObject {

    private ViewPanel view;
    private float width, height;

    public Node root;

    private float nodeTimer;
    private Random random;

    public Tree(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;
        root = null;

        nodeTimer = 0f;
        random = new Random();

        view.addGameObject(this);

        addValue(50);
        addValue(25);
        addValue(75);
    }

    public void addValue(int value) {
        if (root == null) {
            root = new Node(value, width/2, height/8);
            root.branch = 0;
            view.addGameObject(root);
        } else {
            Node node = new Node(value, width, height);
            view.addGameObject(node);
            root.addNode(node);
        }
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
    public void draw(Canvas canvas) {

    }

    @Override
    public void update(float delta) {
        nodeTimer += delta;
        if (nodeTimer > 1) {
            nodeTimer = 0;
            addValue(random.nextInt(100));
        }
    }
}
