package com.kehxstudios.insight.starfield;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.WindowManager;

import com.kehxstudios.insight.tools.GameActivity;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-18.
 */

public class StarfieldActivity extends GameActivity {

    private ArrayList<Star> stars;
    private float starSpeed, starCount;
    private Paint backgroundPaint, starPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void init() {
        starSpeed = 40f;
        starCount = 1000f;
        stars = new ArrayList<>();
        for (int i = 0; i < starCount; i++) {
            stars.add(new Star(random.nextFloat() * width  - width/2, random.nextFloat() * height - height/2,
                    random.nextFloat() * height));
        }
    }

    @Override
    protected void start() {}

    @Override
    public void update(float delta) {
        for (Star star : stars) {
            star.previousDepth = star.depth;
            star.depth -= starSpeed;
            if (star.depth < 0) {
                star.reset(random.nextFloat() * width  - width/2, random.nextFloat() * height - height/2,
                        height);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        canvas.translate(width/2, height/2);
        for (Star star : stars) {
            float sx = star.position.x / star.depth * width;
            float sy = star.position.y / star.depth * height;
            float px = star.position.x / star.previousDepth * width;
            float py = star.position.y / star.previousDepth * height;

            canvas.drawLine(sx, sy, px, py, starPaint);
        }
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);

        starPaint = new Paint();
        starPaint.setColor(Color.WHITE);
        starPaint.setStyle(Paint.Style.STROKE);
        starPaint.setStrokeWidth(5f);
    }

}
