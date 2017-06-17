package com.kehxstudios.insight.evolutionarySteering;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.Vector2;
import com.kehxstudios.insight.tools.ViewPanel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Map implements GameObject {

    ViewPanel view;
    float width, height;
    ArrayList<Vehicle> vehicles;
    ArrayList<Food> foods;
    ArrayList<Poison> poisons;

    public float vehicleWidth, vehicleHeight;
    public float foodRadius;

    private Paint vehiclePaint, foodPaint, poisonPaint;

    private Random random = new Random();

    public Map(ViewPanel view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;

        view.addGameObject(this);
        createPaints();

        vehicleWidth = width/20;
        vehicleHeight = height/20;

        vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(width/4, height/4, random.nextFloat() * 10 - 5, random.nextFloat() * 10 - 5));

        Vehicle.maxSpeed = width/50;
        Vehicle.maxForce = Vehicle.maxSpeed/20;

        randomFoods();
        randomPoisons();
        foodRadius = width/60;
    }

    private void randomFoods() {
        foods = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            foods.add(new Food(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private void randomPoisons() {
        poisons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            poisons.add(new Poison(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private void newFood() {
        foods.add(new Food(random.nextInt((int)width), random.nextInt((int)height)));
    }

    @Override
    public void draw(Canvas canvas) {
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

            canvas.drawPath(path, vehiclePaint);

            canvas.restore();
        }

        for (Food food : foods) {
            canvas.drawCircle(food.position.x, food.position.y, foodRadius, foodPaint);
        }
        for (Poison poison : poisons) {
            canvas.drawCircle(poison.position.x, poison.position.y, foodRadius, poisonPaint);
        }
    };

    private void assignTargetFood(Vehicle vehicle) {
        float distance = height;
        Food closest = foods.get(0);
        for (Food food : foods) {
            float currentDistance = food.position.distance(vehicle.position);
            if (currentDistance < distance) {
                distance = currentDistance;
                closest = food;
            }
        }
        if (distance < foodRadius*2) {
            foods.remove(closest);
            newFood();
            assignTargetFood(vehicle);
        } else {
            vehicle.target = closest;
        }
    }

    @Override
    public void update(float delta) {
        for (Vehicle vehicle : vehicles) {
            assignTargetFood(vehicle);

            Vector2 desired = Vector2.getDirection(vehicle.position, vehicle.target.position);
            desired.scale(vehicle.maxSpeed);
            desired.sub(vehicle.velocity);
            desired.limit(vehicle.maxForce);

            vehicle.acceleration.add(desired);

            vehicle.velocity.add(vehicle.acceleration);
            vehicle.velocity.limit(vehicle.maxSpeed);
            vehicle.position.add(vehicle.velocity);
            vehicle.acceleration.set(0,0);
        }
    }

    private void createPaints() {
        vehiclePaint = new Paint();
        vehiclePaint.setColor(Color.BLACK);
        vehiclePaint.setStyle(Paint.Style.STROKE);
        vehiclePaint.setStrokeWidth(10f);

        foodPaint = new Paint();
        foodPaint.setColor(Color.GREEN);
        foodPaint.setStyle(Paint.Style.FILL);

        poisonPaint = new Paint();
        poisonPaint.setColor(Color.RED);
        poisonPaint.setStyle(Paint.Style.FILL);
    }
}
