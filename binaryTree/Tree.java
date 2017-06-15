package com.kehxstudios.insight.binaryTree;

import com.kehxstudios.insight.tools.ViewPanel;

/**
 * Created by ReidC on 2017-06-10.
 */

public class Tree {

    private ViewPanel view;
    private float width, height;

    public Node root;

    public Tree(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;
        root = null;
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
}
