package com.shojabon.man10gachav2.data;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by sho on 2018/07/09.
 */
class GachaSound {
    Sound sound;
    float volume;
    float pitch;

    public GachaSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void playSoundToPlayer(Player player){
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void playSoundToServer(){
        for (Player p : Bukkit.getServer().getOnlinePlayers()){
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }

    public void playSoundToServerExeptPlayer(Player player){
        for(Player pp: Bukkit.getServer().getOnlinePlayers()){
            if(!pp.equals(player)){
                pp.playSound(pp.getLocation(), sound, volume, pitch);
            }
        }
    }
}
