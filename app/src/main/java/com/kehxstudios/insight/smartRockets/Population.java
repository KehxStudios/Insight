package com.kehxstudios.insight.smartRockets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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

    public float rocketWidth = 50f;
    public float rocketHeight = 200f;

    public final int populationSize = 20;
    public final float populationLifetime = 10f;

    public float width, height;

    public float currentLifetime;
    public float geneUpdateTimer;
    public float timePerGene;
    public int currentGene;
    public int generation;
    public int completeCount;

    public Rocket[] rockets;
    public ArrayList<Rocket> matingPool;

    public Barrier[] barriers;

    public Vector2 target;

    public Paint rocketPaint, targetPaint, barrierPaint;

    public Population(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;
        currentLifetime = 0f;
        geneUpdateTimer = 0f;
        currentGene = 0;
        generation = 0;

        DNA.geneRange = width/100;
        DNA.geneCount = (int)populationLifetime * 5;
        timePerGene = 1f/5f;

        target = new Vector2(width/2, height/10);

        randomRockets();

        barriers = new Barrier[1];
        barriers[0] = new Barrier(width/2, height/3, width/3, height/40);
        view.addGameObject(this);

        rocketPaint = new Paint();
        rocketPaint.setColor(Color.RED);
        rocketPaint.setStyle(Paint.Style.STROKE);
        rocketPaint.setStrokeWidth(12);

        targetPaint = new Paint();
        targetPaint.setColor(Color.BLUE);
        targetPaint.setStyle(Paint.Style.FILL);

        barrierPaint = new Paint();
        barrierPaint.setColor(Color.BLACK);
        barrierPaint.setStyle(Paint.Style.FILL);
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
            Log.d("Rocket.position", "x: " + rockets[i].position.x + ", y: " + rockets[i].position.y);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(target.x, target.y, 40, targetPaint);
        for (Rocket rocket : rockets) {
            canvas.save();
            canvas.rotate(rocket.getAngle(), rocket.position.x, rocket.position.y);
            canvas.drawRect(rocket.position.x - rocketWidth/2, rocket.position.y - rocketHeight/2,
                    rocket.position.x + rocketWidth/2, rocket.position.y + rocketHeight/2, rocketPaint);
            canvas.restore();
        }
        for (Barrier barrier : barriers) {
            canvas.drawRect(barrier.position.x - barrier.width/2, barrier.position.y - barrier.height/2,
                    barrier.position.x + barrier.width/2, barrier.position.y + barrier.height/2, barrierPaint);
        }
    }

    @Override
    public void update(float delta) {
        currentLifetime += delta;
        geneUpdateTimer += delta;
        if (geneUpdateTimer >= timePerGene) {
            geneUpdateTimer =- timePerGene;
            currentGene++;

            if (currentGene >= DNA.geneCount) {
                Log.d("currentGene", "Above MAX");
                currentGene = 0;
            }
            for (Rocket rocket : rockets) {
                if (!rocket.completed && !rocket.crashed) {
                    rocket.acceleration.set(rocket.dna.genes[currentGene]);
                    rocket.velocity.add(rocket.acceleration);
                    rocket.velocity.limit(rocket.maxVelocity);
                }
            }
        }
        if (currentLifetime < populationLifetime) {
            Log.d("Update", "Begin_" + delta);
            int movingCount = 0;
            for (Rocket rocket : rockets) {
                if (!rocket.completed && !rocket.crashed) {
                    rocket.position.add(rocket.velocity);
                    Log.d("Rocket.position", "x: " + rocket.position.x + ", y: " + rocket.position.y);
                    if (barriers.length > 0) {
                        for (Barrier barrier : barriers) {
                            if (rocket.position.x > barrier.position.x - barrier.width / 2 &&
                                    rocket.position.x < barrier.position.x + barrier.width / 2 &&
                                    rocket.position.y > barrier.position.y - barrier.height / 2 &&
                                    rocket.position.y < barrier.position.y + barrier.height / 2) {
                                rocket.crashed = true;
                            }
                        }
                    }
                    if (rocket.position.x > width ||
                            rocket.position.x < 0 ||
                            rocket.position.y > height ||
                            rocket.position.y < 0) {
                        rocket.crashed = true;
                    }

                    rocket.checkTarget(target);
                    if (!rocket.completed && !rocket.crashed) {
                        movingCount++;
                    }
                }
            }
            if (movingCount == 0) {
                checkGeneration();
            }
        } else {
            checkGeneration();
        }
        Log.d("Update", "End");
    }

    private void checkGeneration() {
        checkCompleted();
        if (completeCount > populationSize/5) {
            restartGeneration();
        } else {
            nextGeneration();
        }
    }

    private void nextGeneration() {
        evaluate();
        selection();
        generation++;
        currentLifetime = 0f;
        geneUpdateTimer = 0f;
    }

    private void restartGeneration() {
        randomRockets();
        generation = 0;
        currentLifetime = 0f;
        geneUpdateTimer = 0f;
    }
}
