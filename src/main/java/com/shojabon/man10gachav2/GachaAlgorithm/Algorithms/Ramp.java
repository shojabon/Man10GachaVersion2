package com.shojabon.man10gachav2.GachaAlgorithm.Algorithms;

import org.bukkit.Bukkit;

public class Ramp {
    long speed;
    double speedNow;
    public Ramp(double speedNow, long speed){
        this.speed = speed;
        this.speedNow = speedNow;
    }

    public double updateTime(){
        return 1000/(speedNow - sigmoid(speed));
    }

    private double sigmoid(double x){
        return 0.4/(1 + Math.exp(-x + 20)) + 0.1;
    }
}
