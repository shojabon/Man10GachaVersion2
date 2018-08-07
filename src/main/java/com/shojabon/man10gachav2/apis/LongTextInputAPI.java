package com.shojabon.man10gachav2.apis;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiFunction;
import java.util.function.Function;

public class LongTextInputAPI {
    Listener listener = new Listener();
    JavaPlugin plugin;
    Player p;
    BiFunction<Player, String, String> okFunction;
    Function<Player, String> cancelFunction;
    public LongTextInputAPI(Player p, String title, BiFunction<Player, String, String> okFunction, Function<Player, String> cancelFunction){
        p.closeInventory();
        this.p = p;
        this.okFunction = okFunction;
        this.cancelFunction = cancelFunction;
        this.plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Man10GachaV2");
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        for(int i = 0;i < 15;i++){
            p.sendMessage(" ");
        }
        IChatBaseComponent component = IChatBaseComponent.ChatSerializer.a("[\"\",{\"text\":\"入力例 /<入力パラメータ> 最初にスラッシュを入れてください\\nキャンセルするには/cancelと入力してくださいもしくは\\nキャンセルをクリックしてください\",\"color\":\"dark_aqua\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"クリックして/を入力\"}},{\"text\":\"\\n\"},{\"text\":\"[キャンセル]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/cancel\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"クリックしてキャンセル\"}}]");
        PacketPlayOutChat packet = new PacketPlayOutChat(component, ChatMessageType.CHAT);
        ((CraftPlayer)(p)).getHandle().playerConnection.sendPacket(packet);
        p.sendMessage("");
        p.sendMessage(title);
        p.sendMessage("");
    }

    private void close(){
        HandlerList.unregisterAll(listener);
    }

    class Listener implements org.bukkit.event.Listener
    {

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent e){
            if(e.getPlayer().getUniqueId() != p.getUniqueId()) return;
            e.setCancelled(true);
            String c = e.getMessage().substring(1);
            if(e.getMessage().equalsIgnoreCase("/cancel")) {
                cancelFunction.apply(e.getPlayer());
                close();
            }else{
                String res = okFunction.apply(e.getPlayer(), c);
                if(res == null){
                    close();
                }
            }
        }

    }
}
