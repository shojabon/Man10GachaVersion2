package com.shojabon.man10gachav2;

import com.google.gson.Gson;
import com.shojabon.man10gachav2.apis.*;
import com.shojabon.man10gachav2.data.*;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemStackPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaVaultPayment;
import com.shojabon.man10gachav2.enums.GachaSpinAlgorithm;
import com.shojabon.man10gachav2.event.SignClickEvent;
import com.shojabon.man10gachav2.event.SignDestroyEvent;
import com.shojabon.man10gachav2.event.SignUpdateEvent;
import com.shojabon.man10gachav2.menu.GachaSettingsSelectionMenu;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.zip.GZIPOutputStream;

public final class Man10GachaV2 extends JavaPlugin implements Listener {

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
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
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
        }.runTaskLater(this, 0);
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
            Player p = ((Player)sender);
            /*ArrayList<GachaPayment> payments = new ArrayList<>();
            payments.add(new GachaPayment(new GachaVaultPayment(100)));

            ArrayList<GachaFinalItemStack> items = new ArrayList<>();
            items.add(new GachaFinalItemStack(new GachaItemStack(new ItemStack(Material.DIAMOND)),10));
            GachaSettings settings = new GachaSettings("testteasdawdasd", "test", 0, new GachaSound(Sound.BLOCK_DISPENSER_DISPENSE, 1,0), 10, GachaSpinAlgorithm.RAMP, false, false, false, "",new ItemStack(Material.DIAMOND), 0, 0);
            api.createNewGacha(settings, payments, items);
            */
            new Thread(() -> new GachaSettingsSelectionMenu(p)).start();

        }
        return false;

    }
}
