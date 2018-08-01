package com.shojabon.man10gachav2.menu.GeneralSettings;

import com.shojabon.man10gachav2.apis.SBannerItemStack;
import com.shojabon.man10gachav2.apis.SInventory;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.GachaSettings;
import com.shojabon.man10gachav2.enums.GachaSettingsIcon;
import com.shojabon.man10gachav2.menu.GachaSettingsMenu;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GachaGeneralSettingsMenu {
    Inventory inv;
    Listener listener;
    JavaPlugin plugin;
    String gacha;
    HashMap<GachaSettingsIcon, ItemStack> iconMap = new HashMap<>();
    public GachaGeneralSettingsMenu(String gacha, Player p){
        p.closeInventory();
        this.gacha = gacha;
        iconMap.put(GachaSettingsIcon.GENERAL, new SItemStack(Material.DIAMOND).setDisplayname("§7§n§l主要設定").build());
        iconMap.put(GachaSettingsIcon.SOUND, new SItemStack(Material.NOTE_BLOCK).setDisplayname("§a§n§lサ§b§n§lウ§c§n§lン§d§n§lド§e§n§l設§f§n§l定").build());
        iconMap.put(GachaSettingsIcon.MESSAGE,new SItemStack(Material.BOOK).setDisplayname("§c§n§lメッセージ設定").build());
        iconMap.put(GachaSettingsIcon.PAYOUT, new SItemStack(Material.EMERALD).setDisplayname("§6§n§l払い出し設定").build());
        iconMap.put(GachaSettingsIcon.MISC, new SItemStack(Material.BONE).setDisplayname("§b§n§lその他の設定").build());
        this.plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Man10GachaV2");
        listener = new Listener(p);
        inv = new SInventory(6, "§b§l§" + gacha + "：一般設定").setItem(53, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§c§l§n戻る").build()).
        setItem(new int[]{9,10,11,12,13,14,15,16,17,45,46,47,48,49,50,51,52}, new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDamage(11).build())
                .setItem(new int[]{18,27,36,26,35,36,44},
                        new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname(" ").setDamage(14).build()).setItem(0, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§d§l左").build()).setItem(8, new SItemStack(new SBannerItemStack((short) 4).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLUE, PatternType.CURLY_BORDER)).build()).setDisplayname("§d§l右").build())
                .setItem(1, new SItemStack(Material.DIAMOND).setGlowingEffect(true).setDisplayname("§7§n§l主要設定").build())
                .setItem(2, new SItemStack(Material.BOOK).setDisplayname("§c§n§lメッセージ設定").build())
                .setItem(3, new SItemStack(Material.NOTE_BLOCK).setDisplayname("§a§n§lサ§b§n§lウ§c§n§lン§d§n§lド§e§n§l設§f§n§l定").build())
                .setItem(4, new SItemStack(Material.EMERALD).setDisplayname("§6§n§l払い出し設定").build())
                .setItem(5, new SItemStack(Material.BONE).setDisplayname("§b§n§lその他の設定").build()).build();
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
        boolean miscAnimation = true;
        GachaSettingsIcon currentIcon = GachaSettingsIcon.GENERAL;
        Listener(Player p){
            List<ItemStack> items = new ArrayList<>();
            items.add(new SItemStack(Material.BONE).setDisplayname("§b§n§lその他の設定").build());
            items.add(new SItemStack(Material.CLAY_BALL).setDisplayname("§b§n§lその他の設定").build());
            items.add(new SItemStack(Material.CLAY_BRICK).setDisplayname("§b§n§lその他の設定").build());
            items.add(new SItemStack(Material.ENDER_PEARL).setDisplayname("§b§n§lその他の設定").build());
            items.add(new SItemStack(Material.LAVA_BUCKET).setDisplayname("§b§n§lその他の設定").build());
            this.p = p;
            Thread t = new Thread(() -> {
                while (miscAnimation){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int in = new Random().nextInt(items.size());
                    if(currentIcon == GachaSettingsIcon.MISC){
                        inv.setItem(5, new SItemStack(items.get(in)).setGlowingEffect(true).build());
                    }else{
                        inv.setItem(5, items.get(in));
                    }
                }
            });
            t.start();
        }

        public void clear(){
            for(int i = 0;i < 7;i++){
                inv.setItem(i + 1, new ItemStack(Material.AIR));
            }
            inv.setItem(1, iconMap.get(GachaSettingsIcon.GENERAL));
            inv.setItem(2, iconMap.get(GachaSettingsIcon.MESSAGE));
            inv.setItem(3, iconMap.get(GachaSettingsIcon.SOUND));
            inv.setItem(4, iconMap.get(GachaSettingsIcon.PAYOUT));
            inv.setItem(5, iconMap.get(GachaSettingsIcon.MISC));
        }

        public void render(){
            clear();
            inv.setItem(1, iconMap.get(GachaSettingsIcon.GENERAL));
            inv.setItem(2, iconMap.get(GachaSettingsIcon.MESSAGE));
            inv.setItem(3, iconMap.get(GachaSettingsIcon.SOUND));
            inv.setItem(4, iconMap.get(GachaSettingsIcon.PAYOUT));
            inv.setItem(5, iconMap.get(GachaSettingsIcon.MISC));
            switch (currentIcon){
                case GENERAL:
                    inv.setItem(1, new SItemStack(inv.getItem(1)).setGlowingEffect(true).build());
                    break;
                case MESSAGE:
                    inv.setItem(2, new SItemStack(inv.getItem(2)).setGlowingEffect(true).build());
                    break;
                case SOUND:
                    inv.setItem(3, new SItemStack(inv.getItem(3)).setGlowingEffect(true).build());
                    break;
                case PAYOUT:
                    inv.setItem(4, new SItemStack(inv.getItem(4)).setGlowingEffect(true).build());
                    break;
                case MISC:
                    inv.setItem(5, new SItemStack(inv.getItem(5)).setGlowingEffect(true).build());
                    break;
            }
        }

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(e.getWhoClicked().getUniqueId() != p.getUniqueId()) return;
            e.setCancelled(true);
            if(e.getRawSlot() == 53) new GachaSettingsMenu(gacha, p);
            switch(e.getRawSlot()){
                case 1:
                    currentIcon = GachaSettingsIcon.GENERAL;
                    break;
                case 2:
                    currentIcon = GachaSettingsIcon.MESSAGE;
                    break;
                case 3:
                    currentIcon = GachaSettingsIcon.SOUND;
                    break;
                case 4:
                    currentIcon = GachaSettingsIcon.PAYOUT;
                    break;
                case 5:
                    currentIcon = GachaSettingsIcon.MISC;
                    break;
            }
            render();
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(e.getPlayer().getUniqueId() != p.getUniqueId()) return;
            miscAnimation = false;
            close((Player) e.getPlayer());
        }

    }
}
