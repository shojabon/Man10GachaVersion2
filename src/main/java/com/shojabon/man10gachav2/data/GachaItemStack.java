package com.shojabon.man10gachav2.data;

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
    GachaTitleText playerTitleText;
    GachaTitleText broadcastTitleText;
    ArrayList<ItemStack> items;

    public GachaItemStack(ItemStack item, int amount, ArrayList<ItemStack> outputItems, ArrayList<String> commands, boolean giveItem, String broadcastMessage, String playerMessage, GachaTitleText playerTitleText, GachaTitleText broadcastTitleText){
        this.item = item;
        this.commands = commands;
        this.broadcastMessage = broadcastMessage;
        this.playerMessage = playerMessage;
        this.giveItem = giveItem;
        this.amount = amount;
        this.playerTitleText = playerTitleText;
        this.broadcastTitleText = broadcastTitleText;
        this.items = outputItems;
    }
}
