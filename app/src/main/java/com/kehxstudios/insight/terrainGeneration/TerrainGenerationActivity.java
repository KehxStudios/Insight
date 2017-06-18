package com.kehxstudios.insight.terrainGeneration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.GameView;

/**
 * Created by ReidC on 2017-06-18.
 */

public class TerrainGenerationActivity extends GameActivity {

    private int cols, rows;
    private float scl;
    private Paint backgroundPaint, linePaint;

    @Override
    protected void init() {
        scl = (int)(width/25);
        cols = (int)(width/scl);
        rows = (int)(height/scl);
    }

    @Override
    protected void start() {}

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (int y = 0; y < rows; y++) {
            Path path = new Path();

            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(0, y * scl);
            for (int x = 0; x < cols; x++) {
                path.lineTo(x*scl, y * scl);
                path.lineTo(x*scl, (y + 1) * scl);
                //canvas.drawRect(x*scl, y*scl, x*scl + scl, y*scl + scl, linePaint);
            }
            path.close();
            canvas.drawPath(path, linePaint);
        }
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);

    }

}
