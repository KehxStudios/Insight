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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.kehxstudios.insight.tools.GameActivity;
import com.kehxstudios.insight.tools.GameControl;
import com.kehxstudios.insight.tools.Tools;
import com.kehxstudios.insight.tools.Vector2;

import java.util.ArrayList;

/**
 *
 */

public class Map extends GameControl {

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Vector2> foods;
    private ArrayList<Vector2> poisons;

    private float vehicleWidth, vehicleHeight;
    private float foodRadius;
    private float foodChance, poisonChance;

    private Paint vehiclePaint, foodPaint, poisonPaint, backgroundPaint, debugGreenPaint, debugRedPaint;

    public Map(GameActivity gameActivity, float width, float height) {
        super(gameActivity, width, height);
        vehicleWidth = width/30;
        vehicleHeight = height/30;

        vehicles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            vehicles.add(newVehicle());
        }
        Vehicle.maxSpeed = width/65;
        Vehicle.maxForce = Vehicle.maxSpeed/30;

        randomFoods();
        randomPoisons();
        foodRadius = width/100;

        foodChance = 0.03f;
        poisonChance = 0.01f;


    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (random.nextFloat() < foodChance) {
            newFood();
        }
        if (random.nextFloat() < poisonChance) {
            newPoison();
        }

        for (int i = vehicles.size() - 1; i >= 0; i--) {
            behaviors(vehicles.get(i));
            vehicles.get(i).health -= 0.001f;

            if (vehicles.get(i).health <= 0) {
                foods.add(new Vector2(vehicles.get(i).position));
                vehicles.remove(i);
                continue;
            }

            vehicles.get(i).velocity.add(vehicles.get(i).acceleration);
            vehicles.get(i).velocity.limit(vehicles.get(i).maxSpeed);
            vehicles.get(i).position.add(vehicles.get(i).velocity);
            vehicles.get(i).acceleration.set(0,0);

            if (vehicles.get(i).position.x < 0) {
                vehicles.get(i).position.x += width;
            } else if (vehicles.get(i).position.x > width) {
                vehicles.get(i).position.x -= width;
            }
            if (vehicles.get(i).position.y < 0) {
                vehicles.get(i).position.y += height;
            } else if (vehicles.get(i).position.y > height) {
                vehicles.get(i).position.y -= height;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (Vehicle vehicle : vehicles) {
            canvas.save();
            canvas.rotate(vehicle.getAngle(), vehicle.position.x, vehicle.position.y);

            float x = vehicle.position.x - vehicleWidth/2;
            float y = vehicle.position.y + vehicleHeight/2;

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(x, y);
            path.lineTo(x + vehicleWidth/2, y - vehicleHeight);
            path.lineTo(x + vehicleWidth, y);
            path.close();

            vehiclePaint.setColor(ColorUtils.blendARGB(Color.RED, Color.GREEN, vehicle.health));

            canvas.drawPath(path, vehiclePaint);
            if (debugMode) {
                canvas.drawCircle(vehicle.position.x, vehicle.position.y, vehicle.dna[2], debugGreenPaint);
                canvas.drawCircle(vehicle.position.x, vehicle.position.y, vehicle.dna[3], debugRedPaint);
            }

            canvas.restore();
        }
        if (debugMode) {
            canvas.drawText("Food Chance: " + String.format("%.2f", foodChance), width/2, 80, debugGreenPaint);
            canvas.drawText("Poison Chance: " + String.format("%.2f", poisonChance), width/2, 160, debugRedPaint);
        }

        for (Vector2 food : foods) {
            canvas.drawCircle(food.x, food.y, foodRadius, foodPaint);
        }
        for (Vector2 poison : poisons) {
            canvas.drawCircle(poison.x, poison.y, foodRadius, poisonPaint);
        }
    }

    private Vector2 getFoodSteering(Vehicle vehicle) {
        float distance = height*100;
        Vector2 closest = null;
        for (Vector2 food : foods) {
            float currentDistance = food.distance(vehicle.position);
            if (currentDistance < distance && currentDistance < vehicle.dna[2]) {
                distance = currentDistance;
                closest = food;
            }
        }
        if (closest != null) {
            if (distance < foodRadius * 2) {
                foods.remove(closest);
                vehicle.health += 0.2f;
                if (vehicle.health > 1f) {
                    vehicle.health = 1f;
                }
                return getFoodSteering(vehicle);
            } else {
                return seekTarget(vehicle, closest);
            }
        } else {
            return seekTarget(vehicle, new Vector2(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private Vector2 getPoisonSteering(Vehicle vehicle) {
        float distance = height*100;
        Vector2 closest = null;
        for (Vector2 poison : poisons) {
            float currentDistance = poison.distance(vehicle.position);
            if (currentDistance < distance && currentDistance < vehicle.dna[3]) {
                distance = currentDistance;
                closest = poison;
            }
        }
        if (closest != null) {
            if (distance < foodRadius * 2) {
                poisons.remove(closest);
                vehicle.health -= 0.3f;
                return getPoisonSteering(vehicle);
            } else {
                return seekTarget(vehicle, closest);
            }
        } else {
            return new Vector2(0,0);
        }
    }

    private void vehicleMating(Vehicle vehicle) {
        for (Vehicle veh : vehicles) {
            if (vehicle == veh)
                continue;
            float distance = veh.position.distance(vehicle.position);
            if (distance < vehicleWidth) {
                if (random.nextFloat() < 0.01) {
                    vehicle.clone();
                    vehicle.health -= 0.1f;
                    Log.d("Vehicle", "BABY");
                }
            }
        }
    }

    private Vector2 seekTarget(Vehicle vehicle, Vector2 target) {
        Vector2 desired = Vector2.getDirection(vehicle.position, target);
        desired.scale(vehicle.maxSpeed);
        desired.sub(vehicle.velocity);
        desired.limit(vehicle.maxForce);
        return desired;
    }

    private void behaviors(Vehicle vehicle) {
        Vector2 foodSteer = getFoodSteering(vehicle);
        Vector2 poisonSteer = getPoisonSteering(vehicle);
        foodSteer.scale(vehicle.dna[0]);
        poisonSteer.scale(vehicle.dna[1]);

        vehicleMating(vehicle);

        vehicle.acceleration.add(foodSteer);
        vehicle.acceleration.add(poisonSteer);
    }

    private Vehicle newVehicle() {
        return new Vehicle(width, height);
    }

    private void randomFoods() {
        foods = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            newFood();
        }
    }

    private void newFood() {
        foods.add(randomScreenVector());
    }

    private void randomPoisons() {
        poisons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            newPoison();
        }
    }

    private void newPoison() {
        poisons.add(randomScreenVector());
    }

    private Vector2 randomScreenVector() {
        return new Vector2(random.nextFloat() * width, random.nextFloat() * height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() > width/8*7) {
            foodChance = Tools.map(event.getY(), height, 0, -0.1f, 0.2f);
        } else if (event.getX() < width/8) {
            poisonChance = Tools.map(event.getY(), height, 0, -0.1f, 0.2f);
        } else if (event.getX() > width/8*3 && event.getX() < width/8*5 && event.getY() < height/8) {
            debugMode = !debugMode;
        }
        return false;
    }

    @Override
    protected void createPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);

        vehiclePaint = new Paint();
        vehiclePaint.setColor(Color.GREEN);
        vehiclePaint.setStyle(Paint.Style.STROKE);
        vehiclePaint.setStrokeWidth(15f);

        foodPaint = new Paint();
        foodPaint.setColor(Color.GREEN);
        foodPaint.setStyle(Paint.Style.FILL);

        poisonPaint = new Paint();
        poisonPaint.setColor(Color.RED);
        poisonPaint.setStyle(Paint.Style.FILL);

        debugGreenPaint = new Paint();
        debugGreenPaint.setColor(Color.GREEN);
        debugGreenPaint.setStyle(Paint.Style.STROKE);
        debugGreenPaint.setStrokeWidth(5f);
        debugGreenPaint.setTextAlign(Paint.Align.CENTER);
        debugGreenPaint.setTextSize(60f);

        debugRedPaint = new Paint();
        debugRedPaint.setColor(Color.RED);
        debugRedPaint.setStyle(Paint.Style.STROKE);
        debugRedPaint.setStrokeWidth(5f);
        debugRedPaint.setTextAlign(Paint.Align.CENTER);
        debugRedPaint.setTextSize(60f);
    }
}
