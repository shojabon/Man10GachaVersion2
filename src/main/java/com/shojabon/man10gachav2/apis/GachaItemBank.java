package com.shojabon.man10gachav2.apis;

import org.bukkit.Bukkit;
import red.man10.man10marketextention.Man10ItemBankAPI;

import java.util.UUID;

/**
 * Created by sho on 2018/07/10.
 */
public class GachaItemBank {

    Man10ItemBankAPI itemBankPayment = null;
    boolean enabled = false;
    public GachaItemBank(){
        if(Bukkit.getPluginManager().getPlugin("Man10MarketExtention") != null){
            itemBankPayment = new Man10ItemBankAPI();
            enabled = true;
        }
    }

    public boolean useable(){
        return enabled;
    }

    public void takeItems(UUID uuid, int id, long amount){
        if(!useable())return;
        itemBankPayment.takePlayerItems(uuid, id, amount);
    }

    public void giveItems(UUID uuid, int id, long amount){
        if(!useable())return;
        itemBankPayment.givePlayerItems(uuid, id, amount);
    }

    public boolean hasEnough(UUID uuid, int id, long amount){
        if(!useable())return false;
        return itemBankPayment.playerHasEnoughItems(uuid, id, amount);
    }

    public String getName(int id){
        if(!useable())return "";
        return itemBankPayment.getNameFromId(id);
    }


}
