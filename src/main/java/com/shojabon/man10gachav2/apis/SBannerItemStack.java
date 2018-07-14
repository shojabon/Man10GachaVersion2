package com.shojabon.man10gachav2.apis;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by sho on 2018/07/12.
 */
public class SBannerItemStack {
    ItemStack banner = new ItemStack(Material.BANNER);

    public SBannerItemStack(short color){
        banner.setDurability(color);
    }

    public SBannerItemStack pattern(Pattern pattern) {
        BannerMeta meta = (BannerMeta)banner.getItemMeta();
        meta.addPattern(pattern);
        banner.setItemMeta(meta);
        return this;
    }

    public SBannerItemStack patterns(List<Pattern> patterns){
        BannerMeta meta = (BannerMeta)banner.getItemMeta();
        for(Pattern pat : patterns){
            meta.addPattern(pat);
        }
        banner.setItemMeta(meta);
        return this;
    }

    public ItemStack build(){
        return this.banner;
    }


}
