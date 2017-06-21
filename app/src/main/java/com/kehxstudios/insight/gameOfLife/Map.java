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

package com.kehxstudios.insight.gameOfLife;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.GameControl;

/**
 *
 */

public class Map extends GameControl {

    private int rows, cols, cellSize;
    private Cell[][] grid;

    private Paint backgroundPaint, populatedPaint, emptyPaint;

    public Map(GameActivity gameActivity, float width, float height) {
        super(gameActivity, width, height);
        gameActivity.setUpdatesPerSecond(5f);
        cellSize = (int)(width/50);
        cols = (int)(width/cellSize);
        rows = (int)(height/cellSize) - 1;
        grid = new Cell[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = new Cell(j, i);
                if (random.nextFloat() > 0.6)
                    cell.state = true;
                grid[j][i] = cell;
            }
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = grid[j][i];
                int neighbours = getNeighbourCount(cell);
                if (cell.state) {
                    if (neighbours <= 1)
                        cell.newState = false;
                    else if (neighbours >= 4)
                        cell.newState = false;
                    else
                        cell.newState = true;
                } else if (neighbours == 3) {
                    cell.newState = true;
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[j][i].updateState();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = grid[j][i];
                if (cell.state) {
                    canvas.drawRect(cell.x * cellSize, cell.y * cellSize, cell.x * cellSize + cellSize,
                            cell.y * cellSize + cellSize, populatedPaint);
                } else {
                    canvas.drawRect(cell.x * cellSize, cell.y * cellSize, cell.x * cellSize + cellSize,
                            cell.y * cellSize + cellSize, emptyPaint);
                }
            }
        }
    }

    private int getNeighbourCount(Cell cell) {
        int neighbourCount = 0;
        int x = (int)cell.x;
        int y = (int)cell.y;

        if (y > 0 && grid[x][y-1].state)
            neighbourCount++;
        if (y < rows - 1 && grid[x][y+1].state)
            neighbourCount++;
        if (x > 0) {
            if (grid[x-1][y].state)
                neighbourCount++;
            if (y > 0 && grid[x-1][y-1].state)
                neighbourCount++;
            if (y < rows - 1 && grid[x-1][y+1].state)
                neighbourCount++;
        }
        if (x < cols - 1) {
            if (grid[x+1][y].state)
                neighbourCount++;
            if (y > 0 && grid[x+1][y-1].state)
                neighbourCount++;
            if (y < rows - 1 && grid[x+1][y+1].state)
                neighbourCount++;
        }
        return neighbourCount;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)(event.getX() / cellSize);
        int y = (int)(event.getY() / cellSize);
        grid[x][y].newState = true;
        return false;
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        populatedPaint = new Paint();
        populatedPaint.setColor(Color.DKGRAY);
        populatedPaint.setStyle(Paint.Style.FILL);

        emptyPaint = new Paint();
        emptyPaint.setColor(Color.LTGRAY);
        emptyPaint.setStyle(Paint.Style.STROKE);
        emptyPaint.setStrokeWidth(5f);
    }
}
