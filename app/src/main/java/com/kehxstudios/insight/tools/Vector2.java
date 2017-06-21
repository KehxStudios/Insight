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

package com.kehxstudios.insight.tools;

/**
 *
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
