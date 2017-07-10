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

package com.kehxstudios.insight.evolutionarySteering;

import com.kehxstudios.insight.tools.Tools;
import com.kehxstudios.insight.tools.Vector2;

import java.util.Random;

/**
 *
 */

public class Vehicle {

    private static Random random = new Random();

    public Vector2 position, acceleration, velocity;
    public static float maxSpeed = 8f, maxForce = 0.2f;
    public float health;
    public float[] dna;

    public Vehicle(float width, float height) {
        position = new Vector2(random.nextInt((int)width), random.nextInt((int)height));
        acceleration = new Vector2();
        velocity = new Vector2();
        health = 1f;
        dna = new float[4];
        dna[0] = Tools.randomRange(random, -2, 2);
        dna[1] = Tools.randomRange(random, -2, 2);
        dna[2] = Tools.randomRange(random, width/10, width/5);
        dna[3] = Tools.randomRange(random, width/10, width/5);
    }

    public Vehicle(float x, float y, float[] dna) {
        position = new Vector2(x, y);
        acceleration = new Vector2();
        velocity = new Vector2();
        health = 1f;
        this.dna = dna;
        mutate();
    }

    private void mutate() {
        if (random.nextFloat() < 0.2)
            dna[0] += random.nextFloat() - 0.5;
        if (random.nextFloat() < 0.2)
            dna[1] += random.nextFloat() - 0.5;
        if (random.nextFloat() < 0.2)
            dna[2] += random.nextFloat() * 10 - 5;
        if (random.nextFloat() < 0.2)
            dna[3] += random.nextFloat() * 10 - 5;
    }

    public float getAngle() {
        float angle = (float) Math.toDegrees(Math.atan2((double)velocity.y,(double)velocity.x));
        angle += 90;
        if(angle < 0){ angle += 360; }
        return angle;
    }

    public Vehicle clone() {
        return new Vehicle(position.x, position.y, dna);
    }
}
