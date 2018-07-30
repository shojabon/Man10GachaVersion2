package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.*;
import com.shojabon.man10gachav2.event.SignClickEvent;
import com.shojabon.man10gachav2.event.SignDestroyEvent;
import com.shojabon.man10gachav2.event.SignUpdateEvent;
import com.shojabon.man10gachav2.menu.GachaSettingsSelectionMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public final class Man10GachaV2 extends JavaPlugin {

    FileConfiguration pluginConfig = null;

    GachaVault vault = null;
    public Man10GachaAPI api = null;

    DatabaseConnector mysql = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        pluginConfig = getConfig();
        prefix = pluginConfig.getString("prefix").replace("&", "§");
        vault = new GachaVault();
        api = new Man10GachaAPI();
        databaseBootSequence();
        api.createSignsFileIfNotExist();
        api.loadSignFile();
        Bukkit.getServer().getPluginManager().registerEvents(new SignUpdateEvent(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new SignDestroyEvent(this),this);
        Bukkit.getServer().getPluginManager().registerEvents(new SignClickEvent(this),this);
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> names = api.getGachasInDirectory();
                for(String name : names){
                    api.getGacha(name);
                }
            }
        }.runTaskLater(this, 2);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String prefix = "§6[§aMg§fac§dha§5V2§6]§f";

    public void createLog(String message){
        Bukkit.getLogger().info("[Mgachav2]" + message);
    }

    private void databaseBootSequence(){
        if(pluginConfig.getString("database_settings.type").equalsIgnoreCase("mysql")){
            mysql = new DatabaseConnector(pluginConfig.getString("database_settings.host"),
                    pluginConfig.getInt("database_settings.port"),
                    pluginConfig.getString("database_settings.username"),
                    pluginConfig.getString("database_settings.password"),
                    pluginConfig.getString("database_settings.database"));
        }else if(pluginConfig.getString("database_settings.type").equalsIgnoreCase("sqlite")){
            String databaseName = pluginConfig.getString("database_settings.database");
            if(databaseName == null || databaseName.equals("")) databaseName = "database.db";
            if(!databaseName.contains(".db")) databaseName = databaseName + ".db";
            mysql = new DatabaseConnector(new File(getDataFolder(), databaseName));
        }else{
            createLog("database setting is none");
            mysql = null;
            return;
        }
        createLog("connecting to database..." + pluginConfig.getString("database_settings.type"));
        if(mysql.connectable()){
            createLog("connected to database");
            return;
        }
        createLog("failed to connect to database");
        mysql = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gacha")){
            //ArrayList<GachaPayment> payment = new ArrayList<>();
            //payment.add(new GachaPayment(new GachaVaultPayment(1000)));
            //ArrayList<GachaFinalItemStack> items = new ArrayList<>();
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.POTATO)), 10));
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.DIAMOND)), 10));
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.GOLD_INGOT)), 10));
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.IRON_INGOT)), 10));
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.EMERALD)), 10));
            //items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.TNT)), 10));
            //api.createNewGacha(new GachaSettings("test1"), payment, items);

            //GachaGame game = new GachaGame("test1", this);
            //game.play(((Player)sender));
            new GachaSettingsSelectionMenu(((Player)sender));
        }
        return false;
    }
}
