package com.shojabon.man10gachav2.data;

import com.shojabon.man10gachav2.enums.GachaGameState;

import java.util.UUID;

public class GachaGameStateData {
    private UUID uuid;
    private GachaGameState state;
    private String gacha;
    public GachaGameStateData(UUID uuid, GachaGameState state, String gacha){
        this.uuid = uuid;
        this.state = state;
        this.gacha = gacha;
    }

    public UUID getUuid() {
        return uuid;
    }

    public GachaGameState getState() {
        return state;
    }

    public String getGacha() {
        return gacha;
    }

    public void setGacha(String gacha) {
        this.gacha = gacha;
    }

    public void setState(GachaGameState state) {
        this.state = state;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
