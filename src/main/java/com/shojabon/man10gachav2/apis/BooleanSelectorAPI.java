package com.shojabon.man10gachav2.apis;

import com.shojabon.man10gachav2.menu.FifthGenGUITemplate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiFunction;

public class BooleanSelectorAPI {
    Inventory inv;
    Listener listener = new Listener();
    JavaPlugin plugin;
    Player p;
    String title;
    ItemStack itemsToDisplay;
    boolean current;
    BiFunction<InventoryClickEvent, Boolean, String> biFunction;
    public BooleanSelectorAPI(String title, Player p, ItemStack itemToDisplay, boolean current, BiFunction<InventoryClickEvent, Boolean, String> biFunction){
        p.closeInventory();
        this.title = title;
        this.p = p;
        this.itemsToDisplay = itemToDisplay;
        this.plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Man10GachaV2");
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        this.biFunction = biFunction;
        SInventory inv = new SInventory(5, title);
        inv.fillInventory(new SItemStack(Material.STAINED_GLASS_PANE).setDamage( (short) 11).setDisplayname(" ").build());
        inv.setItem(31, new SItemStack(Material.STORAGE_MINECART).setDisplayname("§b§n§l変更を保存する").build());
        inv.setItem(13, itemsToDisplay);
        this.inv = inv.build();
        render(current);
        p.openInventory(this.inv);
    }

    private void render(boolean b){
        ItemStack trueGlass = new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname("§a§lTrue").setDamage((short) 5).build();
        ItemStack falseGlass = new SItemStack(Material.STAINED_GLASS_PANE).setDisplayname("§c§lFalse").setDamage((short) 14).build();
        if(b){
            inv.setItem(29, new SItemStack(trueGlass).setGlowingEffect(true).build());
            inv.setItem(30, new SItemStack(trueGlass).setGlowingEffect(true).build());
            inv.setItem(32, falseGlass);
            inv.setItem(33, falseGlass);
        }else{
            inv.setItem(29, trueGlass);
            inv.setItem(30, trueGlass);
            inv.setItem(32, new SItemStack(falseGlass).setGlowingEffect(true).build());
            inv.setItem(33, new SItemStack(falseGlass).setGlowingEffect(true).build());
        }
    }

    private void close(Player p){
        HandlerList.unregisterAll(listener);
        p.closeInventory();
    }

    class Listener implements org.bukkit.event.Listener
    {

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(e.getWhoClicked().getUniqueId() != p.getUniqueId()) return;
            e.setCancelled(true);
            int s = e.getRawSlot();
            if(s == 31){
                biFunction.apply(e, current);
                return;
            }
            if(s == 29 || s == 30){
                current = true;
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                render(true);
            }else if(s == 33 || s == 32){
                current = false;
                p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                render(false);
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(e.getPlayer().getUniqueId() != p.getUniqueId()) return;
            close((Player) e.getPlayer());
        }

    }
}
