package com.kehxstudios.insight.smartRockets;

import android.graphics.Canvas;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-13.
 */

public class Population implements GameObject {

    private static Random random = new Random();

    public final int populationSize = 5;
    public final float populationLifetime = 5f;

    public float width, height;

    public float currentLifetime;
    public int generation;

    public Rocket[] rockets;
    public ArrayList<Rocket> matingPool;

    public Vector2 target = new Vector2(width/2, height/10);

    public Population(float width, float height) {
        this.width = width;
        this.height = height;
        currentLifetime = 0f;
        generation = 0;

        randomRockets();
    }


    public void evaluate() {
        float maxFit = 0f;

        for (int i = 0; i < rockets.length; i++) {
            rockets[i].calculateFitness();
            if (rockets[i].fitness > maxFit) {
                maxFit = rockets[i].fitness;
            }
        }
        matingPool = new ArrayList<>();
        for (int i = 0; i < rockets.length; i++) {
            rockets[i].fitness /= maxFit;
            int n = (int)(rockets[i].fitness * 10);
            if (n < 1)
                n = 1;
            for (int j = 0; j < n; j++) {
                matingPool.add(rockets[i]);
            }
        }
    }

    public void selection() {
        rockets = new Rocket[populationSize];
        for (int i = 0; i < rockets.length; i++) {
            DNA parentA = matingPool.get(random.nextInt(matingPool.size() - 1)).dna;
            DNA parentB = matingPool.get(random.nextInt(matingPool.size() - 1)).dna;
            while (parentA == parentB) {
                parentB = matingPool.get(random.nextInt(matingPool.size() - 1)).dna;
            }
            DNA child = parentA.crossOver(parentB);
            child.mutation();
            rockets[i] = new Rocket(child, width/2, height/5*4);
        }
    }


    public void randomRockets() {
        rockets = new Rocket[populationSize];
        for (int i = 0; i < rockets.length; i++) {
            rockets[i] = new Rocket(new DNA(), width/2, height/5*4);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Rocket rocket : rockets) {
            rocket.draw(canvas);
        }
    }

    @Override
    public void update(float delta) {
        currentLifetime += delta;
        if (currentLifetime < populationLifetime) {
            for (Rocket rocket : rockets) {
                if (!rocket.completed && !rocket.crashed) {
                    rocket.acceleration.add(rocket.dna.genes[(int) currentLifetime]);
                    rocket.velocity.add(rocket.acceleration);
                    rocket.acceleration.set(0, 0);
                    rocket.velocity.limit(rocket.maxVelocity);
                    rocket.position.add(rocket.velocity);
                    rocket.update(delta);

                    // Check Collisions

                    rocket.checkTarget(target);
                }
            }

        }
    }
}
