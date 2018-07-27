package com.shojabon.man10gachav2.menu;

import com.shojabon.man10gachav2.apis.SInventory;
import com.shojabon.man10gachav2.apis.SItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class GachaSettingSelectionMenu {
    JavaPlugin plugin;
    Player p;
    Listener listener;
    public GachaSettingSelectionMenu(Player p, JavaPlugin plugin){
        this.p = p;
        this.plugin = plugin;
        listener = new Listener(p);
        Bukkit.getPluginManager().registerEvents(listener, Bukkit.getPluginManager().getPlugin("Man10GachaV2"));
        p.openInventory(createInventory());
    }

    private Inventory createInventory(){
        return new SInventory(6, "§b§l設定するガチャを選択してください").setItem(new int[]{53,52}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l次").build()).setItem(new int[]{46,45}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l戻る").build()).setItem(new int[]{51,50,48,47}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(3).setDisplayname(" ").build()).setItem(49, new SItemStack(Material.NETHER_STAR).setDisplayname("§8§l設定を開く").build()).build();
    }

    private void close(Player p){
        p.closeInventory();
        HandlerList.unregisterAll(listener);
    }

    class Listener implements org.bukkit.event.Listener {
        Player p;
        Listener(Player p){
            this.p = p;
        }
        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(e.getWhoClicked().getUniqueId() != p.getUniqueId()) return;
            e.setCancelled(true);
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            close((Player) e.getPlayer());
        }
    }
}
