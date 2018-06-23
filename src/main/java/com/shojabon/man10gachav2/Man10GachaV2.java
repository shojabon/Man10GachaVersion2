package com.shojabon.man10gachav2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Man10GachaV2 extends JavaPlugin {

    FileConfiguration pluginConfig = null;

    GachaVault vault = null;
    Man10GachaAPI api = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        pluginConfig = getConfig();
        prefix = pluginConfig.getString("prefix");
        vault = new GachaVault();
        api = new Man10GachaAPI();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    String prefix = "§a[§dMan10Gacha§eV2§a]§f";

    public void createLog(String message){
        Bukkit.getLogger().info(prefix + message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gacha")){
            ItemStack stone = new SItemStack(Material.STONE).setDisplayname("§4test").build();
            ItemStack diamon  = new SItemStack(Material.DIAMOND).setDisplayname("§4diamond").build();
            GachaItemStack stoneG = new GachaItemStack(stone, 10, null, null, null, true);
            GachaItemStack diamondG  = new GachaItemStack(diamon, 1, null, null, null, true);
            ArrayList<GachaItemStack> items = new ArrayList<>();
            items.add(stoneG);
            items.add(stoneG);
            items.add(diamondG);
            api.createNewGacha("test", "test", items);
        }
        return false;
    }
}
