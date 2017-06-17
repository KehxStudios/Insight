package com.kehxstudios.insight.aStarPathfinding;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Node {

    public float x, y;
    public float f, g, h;
    public boolean wall;
    public ArrayList<Node> neighbours;
    public Node parent;

    public Node(float x, float y) {
        this.x = x;
        this.y = y;
        wall = false;
        neighbours = new ArrayList<>();
    }
}
