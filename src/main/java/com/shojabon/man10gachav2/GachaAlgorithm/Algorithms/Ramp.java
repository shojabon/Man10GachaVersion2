package com.shojabon.man10gachav2.GachaAlgorithm.Algorithms;

public class Ramp {
    long speed;
    double speedNow;
    public Ramp(double speedNow, long speed){
        this.speed = speed;
        this.speedNow = speedNow;
    }

    public double updateTime(){
        if(speed <= 50){
            return 1000/(speedNow - 0.2f);
        }else{
            return 1000/(speedNow - 0.5f);
        }
    }
}
