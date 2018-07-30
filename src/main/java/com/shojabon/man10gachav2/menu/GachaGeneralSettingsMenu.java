package com.shojabon.man10gachav2.menu;

import com.shojabon.man10gachav2.apis.SBannerItemStack;
import com.shojabon.man10gachav2.apis.SInventory;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.GachaSettings;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GachaGeneralSettingsMenu {
    Inventory inv;
    Listener listener;
    JavaPlugin plugin;
    String gacha;
    public GachaGeneralSettingsMenu(String gacha, Player p){
        p.closeInventory();
        this.gacha = gacha;
        this.plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Man10GachaV2");
        listener = new Listener(p);
        inv = new SInventory(6, "§b§l§" + gacha + "：一般設定").setItem(53, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§c§l§n戻る").build()).
        setItem(new int[]{9,10,11,12,13,14,15,16,17,45,46,47,48,49,50,51,52}, new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDamage(11).build())
                .setItem(new int[]{18,27,36,26,35,36,44},
                        new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDamage(14).build()).setItem(0, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§d§l左").build()).setItem(8, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§d§l右").build()).build();
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        p.openInventory(inv);
    }

    private void close(Player p){
        HandlerList.unregisterAll(listener);
        p.closeInventory();
    }

    class Listener implements org.bukkit.event.Listener
    {
        Player p;
        Listener(Player p){
            this.p = p;
        }

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(e.getWhoClicked().getUniqueId() != p.getUniqueId()) return;
            e.setCancelled(true);
            if(e.getRawSlot() == 53) new GachaSettingsMenu(gacha, p);
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(e.getPlayer().getUniqueId() != p.getUniqueId()) return;
            close((Player) e.getPlayer());
        }

    }
}
