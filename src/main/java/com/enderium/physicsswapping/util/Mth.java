package com.enderium.physicsswapping.util;

public class Mth {


    public static int clamp(int value, int min, int max){
        //? if >=1.21.1 {
        return Math.clamp(value,min,max);
        //?} else
        //return Math.min(Math.max(value,min),max);
    }

    public static long clamp(long value, long min, long max){
        //? if >=1.21.1 {
        return Math.clamp(value,min,max);
         //?} else
        //return Math.min(Math.max(value,min),max);
    }

    public static float clamp(float value, float min, float max){
        //? if >=1.21.1 {
        return Math.clamp(value,min,max);
         //?} else
        //return Math.min(Math.max(value,min),max);
    }

    public static double clamp(double value, double min, double max){
        //? if >=1.21.1 {
        return Math.clamp(value,min,max);
         //?} else
        //return Math.min(Math.max(value,min),max);
    }

}
