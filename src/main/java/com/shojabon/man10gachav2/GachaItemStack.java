package com.shojabon.man10gachav2;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by sho on 2018/06/23.
 */
public class GachaItemStack {
    ItemStack item;
    ArrayList<String> commands;
    String broadcastMessage;
    String playerMessage;
    boolean giveItem;
    int amount;

    public GachaItemStack(ItemStack item, int amount, ArrayList<String> commands, String broadcastMessage, String playerMessage, boolean giveItem){
        this.item = item;
        this.commands = commands;
        this.broadcastMessage = broadcastMessage;
        this.playerMessage = playerMessage;
        this.giveItem = giveItem;
        this.amount = amount;
    }
}
