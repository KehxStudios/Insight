package com.kehxstudios.insight.mazeGenerator;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Cell {

    int row, col;
    boolean top, bottom, left, right;
    boolean visited;

    public Cell(int col, int row) {
        this.col = col;
        this.row = row;
        top = true;
        bottom = true;
        left = true;
        right = true;
        visited = false;
    }

}
