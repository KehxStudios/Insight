package com.kehxstudios.insight.evolutionarySteering;

import com.kehxstudios.insight.tools.Vector2;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Vehicle {

    public Vector2 position, acceleration, velocity;
    public static float maxSpeed = 8f, maxForce = 0.2f;
    public Food target;
    public float[] dna;

    public Vehicle(float x, float y, float dna1, float dna2) {
        position = new Vector2(x, y);
        acceleration = new Vector2();
        velocity = new Vector2();
        target = null;
        dna = new float[2];
        dna[0] = dna1;
        dna[1] = dna2;
    }

    public float getAngle() {
        float angle = (float) Math.toDegrees(Math.atan2((double)velocity.y,(double)velocity.x));
        angle += 90;
        if(angle < 0){ angle += 360; }
        return angle;
    }

    public void behavior() {

    }
}
