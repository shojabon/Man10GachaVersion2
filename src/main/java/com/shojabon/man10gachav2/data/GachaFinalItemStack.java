package com.shojabon.man10gachav2.data;

/**
 * Created by sho on 2018/07/11.
 */
public class GachaFinalItemStack {
    GachaItemStack item;
    int amount;
    public GachaFinalItemStack(GachaItemStack itemStack, int amount){
        this.item = itemStack;
        this.amount = amount;
    }

    public GachaItemStack getItemStack(){
        return this.item;
    }

    public int getAmount(){
        return this.amount;
    }
}
