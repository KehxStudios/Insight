package com.kehxstudios.insight.smartRockets;

import android.util.Log;

import com.kehxstudios.insight.tools.Vector2;

/**
 * Created by ReidC on 2017-06-13.
 */

public class Rocket {

    public static final int width = 10;
    public static final int height = 50;
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
        if (distance < 10) {
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
}
