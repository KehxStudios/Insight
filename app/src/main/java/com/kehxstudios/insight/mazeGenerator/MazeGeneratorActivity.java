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

package com.kehxstudios.insight.mazeGenerator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.kehxstudios.insight.tools.GameActivity;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-17.
 */

public class MazeGeneratorActivity extends GameActivity {

    int rows, cols, cellSize;
    ArrayList<Cell> grid;
    Paint backgroundPaint, cellPaint, currentPaint;

    Cell currentCell;

    ArrayList<Cell> stack;
    boolean finished;

    @Override
    protected void init() {
        cellSize = (int)(width/20);
        cols = (int)(width/cellSize);
        rows = (int)(height/cellSize) - 1;
        grid = new ArrayList<>();
        stack = new ArrayList<>();
        finished = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid.add(new Cell(j, i));
            }
        }

        gameView.setUpdatesPerSecond(20f);

        currentCell = grid.get(0);
    }

    @Override
    protected void start() {

    }

    @Override
    public void update(float delta) {
        if (!finished) {
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

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (Cell cell : grid) {
            int x = cell.col*cellSize;
            int y = cell.row*cellSize;
            if (cell.visited) {
                if (cell.left) {
                    canvas.drawLine(x, y, x, y + cellSize, cellPaint);
                }
                if (cell.right) {
                    canvas.drawLine(x + cellSize, y, x + cellSize, y + cellSize, cellPaint);
                }
                if (cell.top) {
                    canvas.drawLine(x, y, x + cellSize, y, cellPaint);
                }
                if (cell.bottom) {
                    canvas.drawLine(x, y + cellSize, x + cellSize, y + cellSize, cellPaint);
                }
            } else {
                canvas.drawRect(x, y, x + cellSize, y + cellSize, cellPaint);
            }
        }
        canvas.drawRect(currentCell.col * cellSize, currentCell.row * cellSize,
                currentCell.col * cellSize + cellSize, currentCell.row * cellSize + cellSize, currentPaint);
    }


    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        currentPaint = new Paint();
        currentPaint.setColor(Color.BLUE);
        currentPaint.setStyle(Paint.Style.FILL);

        cellPaint = new Paint();
        cellPaint.setColor(Color.WHITE);
        cellPaint.setStyle(Paint.Style.STROKE);
        cellPaint.setStrokeWidth(5f);
    }
}
