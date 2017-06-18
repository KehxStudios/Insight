package com.kehxstudios.insight.mazeGenerator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.GameView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Maze implements GameObject {

    GameView view;
    float width, height;
    int rows, cols, cellSize;
    ArrayList<Cell> grid;
    Paint currentPaint, unvisitedPaint, visitedPaint, backgroundPaint;

    Cell currentCell;
    float mazeTimer;

    ArrayList<Cell> stack;
    boolean finished;

    Random random = new Random();

    public Maze(GameView view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;

        mazeTimer = 0f;
        cellSize = (int)(width/20);
        cols = (int)(width - cellSize*2)/cellSize;
        rows = (int)(height - cellSize*3)/cellSize;
        grid = new ArrayList<>();
        stack = new ArrayList<>();
        finished = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid.add(new Cell(j, i));
            }
        }
        currentPaint = new Paint();
        currentPaint.setColor(Color.BLUE);
        currentPaint.setStyle(Paint.Style.FILL);

        unvisitedPaint = new Paint();
        unvisitedPaint.setColor(Color.BLACK);
        unvisitedPaint.setStyle(Paint.Style.STROKE);
        unvisitedPaint.setStrokeWidth(5f);

        visitedPaint = new Paint();
        visitedPaint.setColor(Color.BLACK);
        visitedPaint.setStyle(Paint.Style.STROKE);
        visitedPaint.setStrokeWidth(5f);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        view.addGameObject(this);

        currentCell = grid.get(0);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (Cell cell : grid) {
            int x = cell.col*cellSize + cellSize;
            int y = cell.row*cellSize + cellSize;
            if (cell.visited) {
                if (cell.left) {
                    canvas.drawLine(x, y, x, y + cellSize, visitedPaint);
                }
                if (cell.right) {
                    canvas.drawLine(x + cellSize, y, x + cellSize, y + cellSize, visitedPaint);
                }
                if (cell.top) {
                    canvas.drawLine(x, y, x + cellSize, y, visitedPaint);
                }
                if (cell.bottom) {
                    canvas.drawLine(x, y + cellSize, x + cellSize, y + cellSize, visitedPaint);
                }
            } else {
                canvas.drawRect(x, y, x + cellSize, y + cellSize, unvisitedPaint);
            }
        }
        canvas.drawRect(currentCell.col * cellSize + cellSize, currentCell.row * cellSize + cellSize,
                currentCell.col * cellSize + cellSize*2, currentCell.row * cellSize + cellSize*2, currentPaint);
    }

    @Override
    public void update(float delta) {
        if (!finished) {
            mazeTimer += delta;
            if (mazeTimer > 0f) {
                mazeTimer = 0f;
                currentCell.visited = true;
                Cell next = checkNeighbours();
                if (next != null) {
                    removeWalls(currentCell, next);
                    stack.add(currentCell);
                    currentCell = next;
                } else if (stack.size() > 0) {
                    currentCell = stack.get(stack.size() - 1);
                    stack.remove(stack.size() - 1);
                } else {
                    finished = true;
                }
            }
        }
    }

    private void removeWalls(Cell current, Cell next) {
        int xDiff = current.col - next.col;
        if (xDiff == -1) {
            current.right = false;
            next.left = false;
        } else if (xDiff == 1) {
            current.left = false;
            next.right = false;
        } else {
            int yDiff = current.row - next.row;
            if (yDiff == -1) {
                current.bottom = false;
                next.top = false;
            } else {
                current.top = false;
                next.bottom = false;
            }
        }
    }

    public Cell checkNeighbours() {
        ArrayList<Cell> neighbours = new ArrayList<>();

        Log.d("CURRENT", currentCell.row + "," + currentCell.col);

        int topIndex = index(currentCell.row - 1, currentCell.col);
        if (topIndex != -1 && !grid.get(topIndex).visited) {
            neighbours.add(grid.get(topIndex));
            Log.d("TOP", grid.get(topIndex).row + "," + grid.get(topIndex).col);
        }
        int rightIndex = index(currentCell.row, currentCell.col + 1);
        if (rightIndex != -1 && !grid.get(rightIndex).visited) {
            neighbours.add(grid.get(rightIndex));
            Log.d("RIGHT", grid.get(rightIndex).row + "," + grid.get(rightIndex).col);
        }
        int bottomIndex = index(currentCell.row + 1, currentCell.col);
        if (bottomIndex != -1 && !grid.get(bottomIndex).visited) {
            neighbours.add(grid.get(bottomIndex));
            Log.d("BOTTOM", grid.get(bottomIndex).row + "," + grid.get(bottomIndex).col);
        }
        int leftIndex = index(currentCell.row, currentCell.col - 1);
        if (leftIndex != -1 && !grid.get(leftIndex).visited) {
            neighbours.add(grid.get(leftIndex));
            Log.d("LEFT", grid.get(leftIndex).row + "," + grid.get(leftIndex).col);
        }

        if (neighbours.size() > 0) {
            return neighbours.get(random.nextInt(neighbours.size()));
        } else {
            return null;
        }
    }

    private int index(int row, int col) {
        if (row < 0 || row > rows - 1 || col < 0 || col > cols - 1) {
            return -1;
        }
        return col + row * cols;
    }
}
