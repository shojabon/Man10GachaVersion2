package com.shojabon.man10gachav2.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by sho on 2018/07/01.
 */
class GachaTitleText {
    String mainText;
    String subText;
    int fadeIntTime;
    int time;
    int fadeoutTime;

    public GachaTitleText(String mainText,String subText, int fadeIntTime, int time, int fadeoutTime){
        this.mainText = mainText;
        this.subText = subText;
        this.fadeIntTime = fadeIntTime;
        this.time = time;
        this.fadeoutTime = fadeoutTime;
    }

    public void playTitleToPlayer(Player p){
        p.sendTitle(mainText, subText,fadeIntTime,time,fadeoutTime);
    }

    public void playTitleToServer(){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            p.sendTitle(mainText, subText, fadeIntTime,time,fadeoutTime);
        }
    }
    public void playTitleToServerExeptPlayer(Player player){
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(!p.equals(player)){
                p.sendTitle(mainText, subText, fadeIntTime,time,fadeoutTime);
            }
        }
    }
}
