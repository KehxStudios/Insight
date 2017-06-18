package com.kehxstudios.insight.tools;

/**
 * Created by ReidC on 2017-06-13.
 */

public class Vector2 {

    public float x, y;

    public Vector2() {
        x = 0f;
        y = 0f;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 vector) {
        x = vector.x;
        y = vector.y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void add(Vector2 vector) {
        x += vector.x;
        y += vector.y;
    }

    public void sub(Vector2 vector) {
        x -= vector.x;
        y -= vector.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 vector) {
        x = vector.x;
        y = vector.y;
    }

    public void normalize(float max, float min) {
        x = (x - min) / (max - min);
        y = (y - min) / (max - min);
    }

    public void scale(float scale) {
        x *= scale;
        y *= scale;
    }

    public void limit(float limit) {
        if (x > limit) {
            x = limit;
        } else if (x < -limit) {
            x = -limit;
        }
        if (y > limit) {
            y = limit;
        } else if (y < -limit) {
            y = -limit;
        }
    }

    public static Vector2 getDirection(Vector2 start, Vector2 target) {
        Vector2 direction = new Vector2(target);
        direction.sub(start);
        direction.normalize(1, -1);
        return direction;
    }

    public float distance(Vector2 target) {
        return (float)Math.sqrt((x - target.x)*(x - target.x) + (y - target.y)*(y - target.y));
    }
}
