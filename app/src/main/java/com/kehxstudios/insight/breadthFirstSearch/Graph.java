package com.kehxstudios.insight.breadthFirstSearch;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-12.
 */

public class Graph {

    public Node start, end;
    public ArrayList<Node> movieNodes;
    public ArrayList<Node> actorNodes;
    private ArrayList<Node> queue;

    public Graph() {
        movieNodes = new ArrayList<>();
        actorNodes = new ArrayList<>();
        queue = new ArrayList<>();
        start = null;
        end = null;
    }

    public void addMovie(Node node) {
        if (!movieNodes.contains(node)) {
            movieNodes.add(node);
        }
    }

    public void addActor(Node node) {
        if (!actorNodes.contains(node)) {
            actorNodes.add(node);
        }
    }

    public Node getActor(String actor) {
        for (Node node : actorNodes) {
            if (node.value.equals(actor)) {
                return node;
            }
        }
        return null;
    }

    public void setStart(Node node) {
        if (actorNodes.contains(node)) {
            start = node;
        } else {
            start = null;
            Log.d("graph", "actor not found");
        }
    }

    public void setEnd(Node node) {
        if (actorNodes.contains(node)) {
            end = node;
        } else {
            end = null;
            Log.d("graph", "actor not found");
        }
    }

    public void search() {
        if (start == null || end == null) {
            Log.d("search", "start or end is null");
            return;
        }
        Log.d("search", "Starting");
        start.searched = true;
        queue.add(start);

        while(queue.size() > 0) {
            Node current = queue.get(0);
            if (current == end) {
                Log.d("Search", "FOUND");
                printSearchResults(current);
                break;
            } else {
                Log.d("Search", "Checking " + current.value + ", edges size: " + current.edges.size());
                for (int i = 0; i < current.edges.size(); i++) {
                    if (!current.edges.get(i).searched) {
                        current.edges.get(i).searched = true;
                        current.edges.get(i).parent = current;
                        queue.add(current.edges.get(i));
                    }
                }
            }
            queue.remove(current);
        }
        for (Node node : actorNodes) {
            node.parent = null;
            node.searched = false;
        }
        for (Node node : movieNodes) {
            node.parent = null;
            node.searched = false;
        }
    }

    public void printSearchResults(Node node) {
        Node current = node;
        while (current.parent != null) {
            Log.d("results", current.value);
            current = current.parent;
        }
        Log.d("result", current.value);
    }
}
