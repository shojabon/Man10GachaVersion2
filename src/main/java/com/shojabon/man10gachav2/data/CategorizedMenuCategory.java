package com.shojabon.man10gachav2.data;

import com.shojabon.man10gachav2.apis.CategorizedMenuAPI;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CategorizedMenuCategory {
    private ItemStack icon;
    private List<ItemStack> content;
    public CategorizedMenuCategory(ItemStack icon, List<ItemStack> content){
        this.icon = icon;
        this.content = content;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<ItemStack> getContent() {
        return content;
    }
}
