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

package com.kehxstudios.insight.smartRockets;

import android.util.Log;

import com.kehxstudios.insight.tools.Vector2;

/**
 * Created by ReidC on 2017-06-13.
 */

public class Rocket {

    public static final float maxVelocity = DNA.geneRange;

    public Vector2 position;
    public Vector2 acceleration;
    public Vector2 velocity;

    public DNA dna;
    public float fitness;
    public float closest;

    public boolean completed, crashed;

    public Rocket(DNA dna, float x, float y) {
        this.dna = dna;
        fitness = 0f;
        closest = 10000f;
        position = new Vector2(x, y);
        acceleration = new Vector2();
        velocity = new Vector2();
        completed = false;
        crashed = false;
    }

    public void checkTarget(Vector2 target) {
        float distance = (float)Math.sqrt((position.x - target.x)*(position.x - target.x) +
                (position.y - target.y)*(position.y - target.y));
        if (distance < 50) {
            position.set(target.x, target.y);
            completed = true;
            Log.d("Rocket", "COMPLETED");
        } else if (distance < closest) {
            closest = distance;
        }
    }

    public void calculateFitness() {
        fitness = (1 / closest * 100);
        if (completed) {
            fitness /= 3;
        } else if (crashed) {
            fitness /= 5;
        } else {
            fitness /= 4;
        }
    }

    public float getAngle() {
        float angle = (float) Math.toDegrees(Math.atan2((double)velocity.y,(double)velocity.x));
        angle += 90;
        if(angle < 0){ angle += 360; }
        return angle;
    }
}
