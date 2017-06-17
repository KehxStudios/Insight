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

    private ArrayList<String> resultPath;

    public Graph() {
        movieNodes = new ArrayList<>();
        actorNodes = new ArrayList<>();
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

    private void reset() {
        for (Node node : actorNodes) {
            node.parent = null;
            node.searched = false;
        }
        for (Node node : movieNodes) {
            node.parent = null;
            node.searched = false;
        }
    }

    public String search() {
        if (start == null || end == null || start == end) {
            Log.d("search", "start or end is null or the same");
            return "Error";
        }
        reset();
        start.searched = true;
        queue = new ArrayList<>();
        queue.add(start);

        while(queue.size() > 0) {
            Node current = queue.get(0);
            if (current == end) {
                return printSearchResults(current);
            } else {
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
        return "Not Found";
    }


    public String printSearchResults(Node node) {
        resultPath = new ArrayList<>();
        resultPath.add(0, node.value);
        Node current = node;
        while (current.parent != null) {
            current = current.parent;
            resultPath.add(0, current.value);
        }
        String result = "";
        for (String path : resultPath) {
            result += path + "\n";
        }
        return result;
    }
}
