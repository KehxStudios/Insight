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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.Vector2;

import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-13.
 */

public class SmartRocketsActivity extends GameActivity {

    public float rocketWidth = 50f;
    public float rocketHeight = 200f;

    public final int populationSize = 20;
    public final float populationLifetime = 10f;

    public float currentLifetime;
    public float geneUpdateTimer;
    public float timePerGene;
    public int currentGene, totalGenes;
    public int generation;
    public int completeCount;

    public Rocket[] rockets;
    public ArrayList<Rocket> matingPool;

    public Barrier[] barriers;

    public Vector2 target;

    public Paint rocketPaint, targetPaint, barrierPaint;

    @Override
    protected void init() {
        currentLifetime = 0f;
        geneUpdateTimer = 0f;
        currentGene = 0;
        generation = 0;

        DNA.geneRange = width/100;
        DNA.geneCount = (int)populationLifetime * 5;
        totalGenes = DNA.geneCount;
        timePerGene = 1f/5f;

        target = new Vector2(width/2, height/10);

        randomRockets();

        barriers = new Barrier[1];
        barriers[0] = new Barrier(width/2, height/3, width/3, height/40);
    }

    @Override
    protected void start() {

    }

    public void evaluate() {
        float maxFit = 0f;

        for (int i = 0; i < rockets.length; i++) {
            calculateFitness(rockets[i]);
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
        canvas.drawText("Generation: " + generation, width/2, 80, barrierPaint);
        canvas.drawText("Time: " + String.format("%.2f", currentLifetime), width/2, 160, barrierPaint);
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
            int movingCount = 0;
            for (Rocket rocket : rockets) {
                if (!rocket.completed && !rocket.crashed) {
                    rocket.position.add(rocket.velocity);
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

                    rocket.checkTarget(target, currentGene);
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
    }

    private void calculateFitness(Rocket rocket) {
        rocket.fitness = (1 / rocket.closest * 10000);
        if (rocket.completed) {
            String log = "COMPLETE\t" + String.format("%.0f", rocket.closest) + " \t" + String.format("%.2f", rocket.fitness) + "\t";
            rocket.fitness += (totalGenes - rocket.completedGene) / 200;
            rocket.fitness /= 3;
            Log.d("Rocket", log + String.format("%.2f", rocket.fitness));
        } else if (rocket.crashed) {
            String log = "CRASHED\t" + String.format("%.0f", rocket.closest) + " \t" + String.format("%.2f", rocket.fitness) + "\t";
            rocket.fitness /= 10;
            Log.d("Rocket", log + String.format("%.2f", rocket.fitness));
        } else {
            String log = "REMAINING\t" + String.format("%.0f", rocket.closest) + " \t" + String.format("%.2f", rocket.fitness) + "\t";
            rocket.fitness += (rocket.closest % (totalGenes - rocket.closestGene)) / 1000;
            rocket.fitness /= 5;
            Log.d("Rocket", log + String.format("%.2f", rocket.fitness));
        }
    }


    private void checkGeneration() {
        checkCompleted();
        if (completeCount > populationSize/10) {
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


    @Override
    protected void createPaints() {
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
        barrierPaint.setTextAlign(Paint.Align.CENTER);
        barrierPaint.setTextSize(80f);
    }
}