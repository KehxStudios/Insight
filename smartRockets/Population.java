package com.kehxstudios.insight.smartRockets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.Vector2;
import com.kehxstudios.insight.tools.ViewPanel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-13.
 */

public class Population implements GameObject {

    private static Random random = new Random();

    public ViewPanel view;

    public final int populationSize = 5;
    public final float populationLifetime = 5f;

    public float width, height;

    public float currentLifetime;
    public int generation;
    public int completeCount;

    public Rocket[] rockets;
    public ArrayList<Rocket> matingPool;

    public Barrier[] barriers;

    public Vector2 target;

    public Paint rocketPaint;

    public Population(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;
        currentLifetime = 0f;
        generation = 0;

        target = new Vector2(width/2, height/10);

        randomRockets();

        barriers = new Barrier[1];
        barriers[0] = new Barrier(width/2, height/2, width/2, height/20);
        view.addGameObject(this);

        rocketPaint = new Paint();
        rocketPaint.setColor(Color.RED);
        rocketPaint.setStyle(Paint.Style.FILL);
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

    public void checkCompleted() {
        completeCount = 0;
        for (Rocket rocket : rockets) {
            if (rocket.completed) {
                completeCount++;
            }
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
        canvas.drawCircle(target.x, target.y, 30, rocketPaint);
        for (Rocket rocket : rockets) {
            canvas.drawRect(rocket.position.x - rocket.width/2, rocket.position.y - rocket.height/2,
                   rocket.width, rocket.height, rocketPaint);
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

                    for (Barrier barrier : barriers) {
                        if (rocket.position.x > barrier.position.x - barrier.width/2 &&
                                rocket.position.x < barrier.position.x + barrier.width/2 &&
                                rocket.position.y > barrier.position.y - barrier.height/2 &&
                                rocket.position.y < barrier.position.y + barrier.height/2) {
                            rocket.crashed = true;
                        }
                    }
                    if (rocket.position.x > width ||
                            rocket.position.x < width ||
                            rocket.position.y > height ||
                            rocket.position.y < height) {
                        rocket.crashed = true;
                    }

                    rocket.checkTarget(target);
                }
            }
        } else {
            checkCompleted();
            if (completeCount > 3) {
                randomRockets();
                generation++;
            } else {
                evaluate();
                selection();
                generation++;
            }
            currentLifetime = 0f;
        }
    }
}
