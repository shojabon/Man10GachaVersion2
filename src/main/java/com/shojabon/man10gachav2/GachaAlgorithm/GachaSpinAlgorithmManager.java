package com.shojabon.man10gachav2.GachaAlgorithm;

import com.shojabon.man10gachav2.GachaAlgorithm.Algorithms.Ramp;
import com.shojabon.man10gachav2.enums.GachaSpinAlgorithm;

public class GachaSpinAlgorithmManager {
    GachaSpinAlgorithm algorithm;
    long speed;
    double speedNow;
    public GachaSpinAlgorithmManager(GachaSpinAlgorithm type, long speed, double speedNow){
        this.algorithm = type;
        this.speed = speed;
        this.speedNow = speedNow;
    }

    public double update(){
        switch (algorithm){
            case RAMP:
                return new Ramp(speedNow, speed).updateTime();
        }
        return 1;
    }




}
