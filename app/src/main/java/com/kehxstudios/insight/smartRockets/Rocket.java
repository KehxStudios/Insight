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

import com.kehxstudios.insight.tools.Vector2;

/**
 *
 */

public class Rocket {

    public static final float maxVelocity = DNA.geneRange;

    public Vector2 position, acceleration, velocity;
    public DNA dna;
    public float fitness, closest;
    public int closestGene, completedGene;
    public boolean completed, crashed;

    public Rocket(DNA dna, float x, float y) {
        this.dna = dna;
        position = new Vector2(x, y);
        acceleration = new Vector2();
        velocity = new Vector2();
        fitness = 0f;
        closest = 10000f;
        closestGene = 0;
        completedGene = 0;
        completed = false;
        crashed = false;
    }

    public void checkTarget(Vector2 target, int currentGene) {
        float distance = position.distance(target);
        if (distance < 50) {
            position.set(target.x, target.y);
            completed = true;
        } else if (distance < closest) {
            closest = distance;
            closestGene = currentGene;
        }
    }

    public float getAngle() {
        float angle = (float) Math.toDegrees(Math.atan2((double)velocity.y,(double)velocity.x));
        angle += 90;
        if(angle < 0){ angle += 360; }
        return angle;
    }
}
