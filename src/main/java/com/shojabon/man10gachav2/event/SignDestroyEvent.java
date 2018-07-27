package com.shojabon.man10gachav2.event;

import com.shojabon.man10gachav2.Man10GachaV2;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignDestroyEvent implements Listener {
    private Man10GachaV2 plugin;

    public SignDestroyEvent(Man10GachaV2 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e){
        if(!plugin.api.ifGachaSign(e.getBlock().getLocation()))return;
        if(!e.getPlayer().hasPermission("mgachav2.sign.destroy")){
            e.getPlayer().sendMessage(plugin.prefix + "§4§lあなたには権限がありません");
            e.setCancelled(true);
            return;
        }
        e.getPlayer().sendMessage(plugin.prefix + "§f§l看板の登録を解除しました");
        plugin.api.deleteSign(e.getBlock().getLocation());
    }
}
