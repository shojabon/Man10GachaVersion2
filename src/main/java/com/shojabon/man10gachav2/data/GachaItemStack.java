package com.shojabon.man10gachav2.data;

import com.shojabon.man10gachav2.apis.SItemStack;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Effect.Type.SOUND;

/**
 * Created by sho on 2018/06/23.
 */
public class GachaItemStack {
    public ItemStack item;
    public ArrayList<String> commands = null;
    public ArrayList<String> broadcastMessage = null;
    public ArrayList<String> playerMessage = null;

    public boolean giveItem = true;
    public GachaTitleText playerTitleText = null;
    public GachaTitleText broadcastTitleText = null;
    public ArrayList<ItemStack> items = null;
    public ArrayList<String> pexGroup = null;
    public ArrayList<String> pexPermission = null;

    public GachaTeleport teleport = null;
    public GachaSound broadcastSound = null;
    public GachaSound playerSound = null;
    public ArrayList<GachaPotionEffect> playerPotionEffect = null;
    public ArrayList<GachaPotionEffect> broadcastPotionEffect = null;

    public long givePlayerMoney = 0;
    public long takePlayerMoney= 0;
    public long giveServerMoney= 0;
    public long takeServerMoney= 0;

    public ArrayList<GachaItemBankData> givePlayerItemBank = null;
    public ArrayList<GachaItemBankData> takePlayerItemBank = null;

    public boolean killPlayer = false;


    public GachaItemStack(ItemStack item){
        this.item = item;
    }
    public GachaItemStack(ItemStack item,
                          ArrayList<ItemStack> outputItems,
                          ArrayList<String> commands,
                          boolean giveItem,
                          ArrayList<String> broadcastMessage,
                          ArrayList<String> playerMessage,
                          GachaTitleText playerTitleText,
                          GachaTitleText broadcastTitleText,
                          ArrayList<String> pexGroup,
                          ArrayList<String> pexPermission,
                          GachaTeleport teleport,
                          GachaSound broadcastSound,
                          GachaSound playerSound,
                          ArrayList<GachaPotionEffect> playerPotionEffect,
                          ArrayList<GachaPotionEffect> broadcastPotionEffect,
                          long givePlayerMoney,
                          long giveServerMoney,
                          long takePlayerMoney,
                          long takeServerMoney,
                          ArrayList<GachaItemBankData> givePlayerItemBank,
                          ArrayList<GachaItemBankData> takePlayerItemBank,
                          boolean killPlayer


    ){
        this.item = item;
        this.commands = commands;
        this.broadcastMessage = broadcastMessage;
        this.playerMessage = playerMessage;
        this.giveItem = giveItem;
        this.playerTitleText = playerTitleText;
        this.broadcastTitleText = broadcastTitleText;
        this.items = outputItems;
        this.pexGroup = pexGroup;
        this.pexPermission = pexPermission;
        this.teleport = teleport;
        this.broadcastSound = broadcastSound;
        this.playerSound = playerSound;
        this.playerPotionEffect = playerPotionEffect;
        this.broadcastPotionEffect = broadcastPotionEffect;
        this.givePlayerMoney = givePlayerMoney;
        this.giveServerMoney = giveServerMoney;
        this.takePlayerMoney = takePlayerMoney;
        this.takeServerMoney = takeServerMoney;
        this.givePlayerItemBank = givePlayerItemBank;
        this.takePlayerItemBank = takePlayerItemBank;
        this.killPlayer = killPlayer;
    }

    public Map<String, Object> getStringData(){
        Map<String, Object> objects = new HashMap<>();
        objects.put("item", new SItemStack(this.item).toBase64());
        if(commands != null){
            objects.put("command", this.commands);
        }
        if(broadcastMessage != null){
            objects.put("broadcastMessage", this.broadcastMessage);
        }
        if(playerMessage != null){
            objects.put("playerMessage", this.playerMessage);
        }
        if(!giveItem){
            objects.put("giveItem", false);
        }
        if(playerTitleText != null){
            objects.put("playerTitleText", this.playerTitleText);
        }
        if(broadcastTitleText != null){
            objects.put("broadcastTitleText", this.broadcastTitleText);
        }
        if(items != null){
            objects.put("items", this.items);
        }
        if(pexGroup != null){
            objects.put("pexGroup", this.pexGroup);
        }
        if(pexPermission != null){
            objects.put("pexPermission", this.pexPermission);
        }
        if(teleport != null){
            objects.put("teleport", this.teleport);
        }
        if(broadcastSound != null){
            objects.put("broadcastSound", this.broadcastSound);
        }
        if(playerSound != null){
            objects.put("playerSound", this.playerSound);
        }
        if(playerPotionEffect != null){
            objects.put("playerPotionEffect", this.playerPotionEffect);
        }
        if(broadcastPotionEffect != null){
            objects.put("broadcastPotionEffect", this.broadcastPotionEffect);
        }
        if(givePlayerMoney != 0){
            objects.put("givePlayerMoney", this.givePlayerMoney);
        }
        if(takePlayerMoney != 0){
            objects.put("takePlayerMoney", this.takePlayerMoney);
        }
        if(giveServerMoney != 0){
            objects.put("giveServerMoney", this.giveServerMoney);
        }
        if(takeServerMoney != 0){
            objects.put("takeServerMoney", this.takeServerMoney);
        }
        if(givePlayerItemBank != null){
            objects.put("givePlayerItemBank", this.givePlayerItemBank);
        }
        if(takePlayerItemBank != null){
            objects.put("takePlayerItemBank", this.takePlayerItemBank);
        }
        if(killPlayer){
            objects.put("killPlayer", true);
        }
        return objects;
    }

}
