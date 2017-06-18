package com.kehxstudios.insight.aStarPathfinding;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.ViewPanel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Grid implements GameObject {

    ViewPanel view;
    float width, height;
    int cols, rows;
    int nodeSize;
    Node[][] nodes;
    boolean allowDiagonal;
    float wallChance = 0.3f;
    float updateTimer, updateTime;

    ArrayList<Node> openSet, closedSet, currentPath;
    Node start, end;

    Paint wallPaint, pathPaint;

    Random random = new Random();

    boolean finished;

    public Grid(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;
        nodeSize = (int)(width/20);
        cols = (int)(width - nodeSize*2)/nodeSize;
        rows = (int)(height - nodeSize*3)/nodeSize;
        finished = false;
        allowDiagonal = false;
        updateTimer = -1f;
        updateTime = 1f;

        populateNodes();

        openSet = new ArrayList<>();
        closedSet = new ArrayList<>();
        start = nodes[0][0];
        start.wall = false;
        end = nodes[cols - 1][rows - 1];
        end.wall = false;

        openSet.add(start);

        view.addGameObject(this);
        createPaints();
    }

    private void reset() {
        populateNodes();

        openSet = new ArrayList<>();
        closedSet = new ArrayList<>();
        start = nodes[0][0];
        start.wall = false;
        end = nodes[cols - 1][rows - 1];
        end.wall = false;

        openSet.add(start);

        updateTimer = -1f;
        finished = false;
        currentPath = null;
    }

    private void populateNodes() {
        nodes = new Node[cols][rows];
        for (int i = 0; i < cols; i++) {
            nodes[i] = new Node[rows];
            for (int j = 0; j < rows; j++) {
                nodes[i][j] = new Node(i, j);
                if (random.nextFloat() < wallChance) {
                    nodes[i][j].wall = true;
                }
            }
        }
        populateNeighbours();
    }

    private void populateNeighbours() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (i < cols - 1) {
                    nodes[i][j].neighbours.add(nodes[i+1][j]);
                }
                if (i > 0) {
                    nodes[i][j].neighbours.add(nodes[i-1][j]);
                }
                if (j < rows - 1) {
                    nodes[i][j].neighbours.add(nodes[i][j+1]);
                }
                if (j > 0) {
                    nodes[i][j].neighbours.add(nodes[i][j-1]);
                }
                if (allowDiagonal) {
                    if (i > 0 && j > 0) {
                        nodes[i][j].neighbours.add(nodes[i - 1][j - 1]);
                    }
                    if (i < cols - 1 && j > 0) {
                        nodes[i][j].neighbours.add(nodes[i + 1][j - 1]);
                    }
                    if (i < cols - 1 && j < rows - 1) {
                        nodes[i][j].neighbours.add(nodes[i + 1][j + 1]);
                    }
                    if (i > 0 && j < rows - 1) {
                        nodes[i][j].neighbours.add(nodes[i - 1][j + 1]);
                    }
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes[i][j].wall) {
                    canvas.drawCircle(nodes[i][j].x * nodeSize + nodeSize/2 + nodeSize,
                            nodes[i][j].y * nodeSize + nodeSize/2 + nodeSize, nodeSize/2, wallPaint);
                }
            }
        }
        if (currentPath != null) {
            for (int i = 1; i < currentPath.size(); i++) {
                canvas.drawLine(currentPath.get(i-1).x * nodeSize + nodeSize/2 + nodeSize, currentPath.get(i-1).y * nodeSize + nodeSize/2 + nodeSize,
                        currentPath.get(i).x * nodeSize + nodeSize/2 + nodeSize, currentPath.get(i).y * nodeSize + nodeSize/2 + nodeSize, pathPaint);
            }
        }

    }

    @Override
    public void update(float delta) {
        updateTimer += delta;
        if (updateTimer < updateTime) {
            return;
        }
        if (!finished) {
            if (openSet.size() > 0) {
                int lowestIndex = 0;
                for (int i = 0; i < openSet.size(); i++) {
                    if (openSet.get(i).f < openSet.get(lowestIndex).f) {
                        lowestIndex = i;
                    }
                }
                Node current = openSet.get(lowestIndex);

                currentPath = new ArrayList<>();
                Node path = current;
                currentPath.add(path);
                while (path.parent != null) {
                    path = path.parent;
                    currentPath.add(path);
                }
                if (current == end) {
                    Log.d("A* Pathfinding", "DONE");
                    currentPath.add(path);
                    finished = true;

                }
                openSet.remove(current);
                closedSet.add(current);

                for (Node node : current.neighbours) {
                    if (closedSet.contains(node) || node.wall) {
                        continue;
                    }
                    float tempG = current.g + 1;
                    if (openSet.contains(node)) {
                        if (tempG < node.g) {
                            node.g = tempG;
                        } else {
                            continue;
                        }
                    } else {
                        node.g = tempG;
                        openSet.add(node);
                    }
                    node.parent = current;
                    node.h = heuristic(node, end);
                    node.f = node.g + node.h;
                }

            } else {
                Log.d("A* Pathfinding", "NO SOLUTION");
                finished = true;
            }
        } else if (updateTimer > updateTime * 3) {
            reset();
        }
    }

    private float heuristic(Node current, Node end) {
        if (allowDiagonal) {

            return (float)Math.sqrt((current.x - end.x)*(current.x - end.x) +
                    (current.y - end.y)*(current.y - end.y));
        } else {
            return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
        }
    }

    private void createPaints() {
        wallPaint = new Paint();
        wallPaint.setStyle(Paint.Style.FILL);
        wallPaint.setColor(Color.BLACK);

        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStrokeWidth(10f);
    }
}