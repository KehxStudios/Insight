package com.kehxstudios.insight.evolutionarySteering;

import com.kehxstudios.insight.tools.Vector2;

import java.util.Random;

/**
 * Created by ReidC on 2017-06-17.
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
        dna[0] = random.nextFloat() * 4 - 2;
        dna[1] = random.nextFloat() * 4 - 2;
        dna[2] = random.nextFloat() * width/6 + width/10;
        dna[3] = random.nextFloat() * width/6 + width/10;
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
            dna[0] += random.nextFloat() * 0.5 - 0.25;
        if (random.nextFloat() < 0.2)
            dna[1] += random.nextFloat() * 0.5 - 0.25;
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
