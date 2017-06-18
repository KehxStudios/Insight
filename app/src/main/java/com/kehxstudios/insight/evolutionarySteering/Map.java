package com.kehxstudios.insight.evolutionarySteering;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.kehxstudios.insight.tools.GameObject;
import com.kehxstudios.insight.tools.Vector2;
import com.kehxstudios.insight.tools.GameView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ReidC on 2017-06-17.
 */

public class Map implements GameObject {

    GameView view;
    float width, height;

    ArrayList<Vehicle> vehicles;
    ArrayList<Food> foods;
    ArrayList<Poison> poisons;

    public float vehicleWidth, vehicleHeight;
    public float foodRadius;

    private Paint vehiclePaint, foodPaint, poisonPaint, backgroundPaint, debugGreen, debugRed;

    private Random random = new Random();

    public Map(GameView view, float width, float height) {
        this.view = view;
        this.width = width;
        this.height = height;

        view.addGameObject(this);
        createPaints();

        vehicleWidth = width/30;
        vehicleHeight = height/30;

        vehicles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            vehicles.add(newVehicle());
        }
        Vehicle.maxSpeed = width/65;
        Vehicle.maxForce = Vehicle.maxSpeed/30;

        randomFoods();
        randomPoisons();
        foodRadius = width/100;
    }

    private void randomFoods() {
        foods = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            foods.add(new Food(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private void newFood() {
        foods.add(new Food(random.nextInt((int)width), random.nextInt((int)height)));
    }

    private void randomPoisons() {
        poisons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            poisons.add(new Poison(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private void newPoison() {
        poisons.add(new Poison(random.nextInt((int)width), random.nextInt((int)height)));
    }

    private Vehicle newVehicle() {
        Log.d("NEW", "VEHICLE");
        return new Vehicle(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
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
            //canvas.drawCircle(vehicle.position.x, vehicle.position.y, vehicle.dna[2], debugGreen);
            //canvas.drawCircle(vehicle.position.x, vehicle.position.y, vehicle.dna[3], debugRed);

            canvas.restore();
        }

        for (Food food : foods) {
            canvas.drawCircle(food.position.x, food.position.y, foodRadius, foodPaint);
        }
        for (Poison poison : poisons) {
            canvas.drawCircle(poison.position.x, poison.position.y, foodRadius, poisonPaint);
        }
    }

    private Vector2 getFoodSteering(Vehicle vehicle) {
        float distance = height*100;
        Food closest = null;
        for (Food food : foods) {
            float currentDistance = food.position.distance(vehicle.position);
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
                return seekTarget(vehicle, closest.position);
            }
        } else {
            return seekTarget(vehicle, new Vector2(random.nextInt((int)width), random.nextInt((int)height)));
        }
    }

    private Vector2 getPoisonSteering(Vehicle vehicle) {
        float distance = height*100;
        Poison closest = null;
        for (Poison poison : poisons) {
            float currentDistance = poison.position.distance(vehicle.position);
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
                return seekTarget(vehicle, closest.position);
            }
        } else {
            return new Vector2(0,0);
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

        vehicle.acceleration.add(foodSteer);
        vehicle.acceleration.add(poisonSteer);
    }

    @Override
    public void update(float delta) {
        if (random.nextFloat() < 0.05) {
            newFood();
        } else if (random.nextFloat() < 0.01) {
            newPoison();
        }

        for (int i = vehicles.size() - 1; i >= 0; i--) {
            behaviors(vehicles.get(i));
            vehicles.get(i).health -= 0.001f;

            if (vehicles.get(i).health <= 0) {
                foods.add(new Food(vehicles.get(i).position.x, vehicles.get(i).position.y));
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

            if (random.nextFloat() < 0.0005) {
                vehicles.add(vehicles.get(i).clone());
                vehicles.get(i).health -= 0.1f;
            }
        }
    }

    private void createPaints() {
        vehiclePaint = new Paint();
        vehiclePaint.setColor(Color.BLACK);
        vehiclePaint.setStyle(Paint.Style.STROKE);
        vehiclePaint.setStrokeWidth(15f);

        foodPaint = new Paint();
        foodPaint.setColor(Color.GREEN);
        foodPaint.setStyle(Paint.Style.FILL);

        poisonPaint = new Paint();
        poisonPaint.setColor(Color.RED);
        poisonPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);

        debugGreen = new Paint();
        debugGreen.setColor(Color.GREEN);
        debugGreen.setStyle(Paint.Style.STROKE);
        debugGreen.setStrokeWidth(10f);

        debugRed = new Paint();
        debugRed.setColor(Color.RED);
        debugRed.setStyle(Paint.Style.STROKE);
        debugRed.setStrokeWidth(10f);
    }
}
