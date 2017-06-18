package com.kehxstudios.insight.breadthFirstSearch;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-12.
 */

public class Node {

    public String value;
    public ArrayList<Node> edges;
    public Node parent;
    public boolean searched;

    public Node(String value) {
        this.value = value;
        edges = new ArrayList<>();
        parent = null;
        searched = false;
    }

    public void addEdge(Node node) {
        if (!edges.contains(node)) {
            edges.add(node);
        }
    }
}
