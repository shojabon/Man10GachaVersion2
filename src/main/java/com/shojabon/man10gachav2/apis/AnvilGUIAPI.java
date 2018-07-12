package com.shojabon.man10gachav2.apis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiFunction;

/**
 * Created by sho on 2018/07/03.
 */
public class AnvilGUIAPI {

    Inventory anvil;

    Listener listener = new Listener();

    Player holder;



    public AnvilGUIAPI(JavaPlugin plugin, Player p, String title, BiFunction<String, String, String> a){
        anvil = Bukkit.createInventory(null, InventoryType.ANVIL);
        ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("");
        item.setItemMeta(itemMeta);
        holder = p;
        anvil.setItem(0, item);
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        p.openInventory(anvil);

    }

    public void closeInventory(){
        HandlerList.unregisterAll(listener);
        holder.closeInventory();
        Bukkit.broadcastMessage(anvil.getItem(2).getItemMeta().getDisplayName());
    }

    private class Listener implements org.bukkit.event.Listener{

        @EventHandler
        public void onClick(InventoryClickEvent e) {
                Bukkit.broadcastMessage(String.valueOf(e.getSlot()));
                e.setCancelled(true);
                Bukkit.broadcastMessage(e.getCurrentItem().getItemMeta().getDisplayName());
                closeInventory();
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            closeInventory();
        }
    }


}
