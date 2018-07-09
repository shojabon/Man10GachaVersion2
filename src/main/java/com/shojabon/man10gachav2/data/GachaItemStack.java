package com.shojabon.man10gachav2.data;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by sho on 2018/06/23.
 */
class GachaItemStack {
    public ItemStack item;
    public ArrayList<String> commands;
    public ArrayList<String> broadcastMessage;
    public ArrayList<String> playerMessage;
    public boolean giveItem;
    public int amount;
    public GachaTitleText playerTitleText;
    public GachaTitleText broadcastTitleText;
    public ArrayList<ItemStack> items;
    public GachaTeleport teleport;
    public GachaSound broadcastSound;
    public GachaSound playerSound;



    public GachaItemStack(ItemStack item, int amount, ArrayList<ItemStack> outputItems,
                          ArrayList<String> commands, boolean giveItem,
                          ArrayList<String> broadcastMessage,
                          ArrayList<String> playerMessage, GachaTitleText playerTitleText,
                          GachaTitleText broadcastTitleText,

    ){
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
