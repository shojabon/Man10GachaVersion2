package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
            GachaGame gacha = new GachaGame("test", this);
            gacha.play(((Player)sender));
        }
        return false;
    }
}
