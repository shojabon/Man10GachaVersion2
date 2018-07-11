package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.AnvilGUIAPI;
import com.shojabon.man10gachav2.apis.DatabaseConnector;
import com.shojabon.man10gachav2.apis.GachaVault;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.GachaFinalItemStack;
import com.shojabon.man10gachav2.data.GachaItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiFunction;

public final class Man10GachaV2 extends JavaPlugin {

    FileConfiguration pluginConfig = null;

    GachaVault vault = null;
    Man10GachaAPI api = null;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    String prefix = "§a[§dMan10Gacha§eV2§a]§f";

    public void createLog(String message){
        Bukkit.getLogger().info(prefix + message);
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
        return;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("gacha")){
            GachaItemStack item1 = new GachaItemStack(new SItemStack(Material.PAPER).setDisplayname("test1").build(),null, null,false,null,null,null,
                    null,null,null,null,null,null,null,null,0,0,0,0
                    ,null,null,false);
            GachaItemStack item2 = new GachaItemStack(new SItemStack(Material.DIAMOND).setDisplayname("test2").build(),null, null,true,null,null,null,
                    null,null,null,null,null,null,null,null,0,1000,0,0
                    ,null,null,false);
            GachaFinalItemStack fItem1 = new GachaFinalItemStack(item1, 10);
            GachaFinalItemStack fItem2 = new GachaFinalItemStack(item2, 30);
            ArrayList<GachaFinalItemStack> items = new ArrayList<>();
            items.add(fItem1);
            items.add(fItem2);
            api.createNewGacha("test", "testt", items);
        }
        return false;
    }
}
