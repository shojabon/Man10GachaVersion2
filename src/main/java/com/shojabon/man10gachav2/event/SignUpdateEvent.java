package com.shojabon.man10gachav2.event;

import com.shojabon.man10gachav2.Man10GachaV2;
import com.shojabon.man10gachav2.data.GachaSignData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SignUpdateEvent implements Listener {
    private Man10GachaV2 plugin;

    public SignUpdateEvent(Man10GachaV2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignUpdate(SignChangeEvent e){
        if(!e.getPlayer().hasPermission("mgachav2.sign.create")) {
            e.getPlayer().sendMessage(plugin.prefix + "§4§lあなたには権限がありません");
            e.setCancelled(true);
            e.getBlock().breakNaturally();
            return;
        }
        if(!e.getLine(0).equalsIgnoreCase("mgachav2")) return;
        if(!plugin.api.ifGachaExists(e.getLine(1))){
            e.getPlayer().sendMessage(plugin.prefix + "§c§lガチャが存在しません");
            e.setCancelled(true);
            e.getBlock().breakNaturally();
            return;
        }
        //register process
        plugin.api.registerNewSign(new GachaSignData(e.getBlock().getLocation(), e.getLine(1)));
        e.setLine(0, "§d§l===============");
        e.setLine(1, "§b§l" + e.getLine(1));
        e.setLine(2, e.getLine(2).replaceAll("&", "§"));
        e.setLine(3, "§d§l===============");
    }

}
