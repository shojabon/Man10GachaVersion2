package com.shojabon.man10gachav2.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class GachaPotionEffect {
    PotionEffectType type;
    int ampli;
    int time;
    public GachaPotionEffect(PotionEffectType type, int ampli, int time){
        this.type = type;
        this.ampli = ampli;
        this.time = time;
    }

    public void effectPlayer(Player player){
        player.addPotionEffect(new PotionEffect(type, time, ampli));
    }

    public void effectServer(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.addPotionEffect(new PotionEffect(type, time, ampli));
        }
    }

    public void effectServerExeptPlayer(Player p){
        for(Player pp : Bukkit.getServer().getOnlinePlayers()){
            if(!pp.equals(p)){
                pp.addPotionEffect(new PotionEffect(type, time, ampli));
            }
        }
    }

    public Map<String, String> getStringData(){
        Map<String, String> out = new HashMap<>();
        out.put("effect", String.valueOf(type));
        out.put("level", String.valueOf(ampli));
        out.put("time", String.valueOf(time));
        return out;
    }


}
