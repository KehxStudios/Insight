package com.kehxstudios.insight.tools;

/**
 * Created by ReidC on 2017-06-19.
 */

public class Tools {


    public static float map(float value, float inMin, float inMax, float outMin, float outMax) {

        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
        //value /= maxValue - minValue;
        //value *= newMax - newMin;
        //return value;
    }
}
