package com.shojabon.man10gachav2.data;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

class GachaPotionEffect {
    PotionEffect potion;
    public GachaPotionEffect(PotionEffect effect){
        this.potion = effect;
    }

    public void effectPlayer(Player player){
        player.addPotionEffect(potion);
    }

    public void effectServer(){
        for()
    }
}
