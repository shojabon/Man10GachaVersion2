package com.shojabon.man10gachav2;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sho on 2018/06/23.
 */
public class Man10GachaAPI {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Man10GachaV2");

    public void createNewGacha(String gachaName, String title,ArrayList<GachaItemStack> itemStacks){
        File file = new File(plugin.getDataFolder(), "gacha" + File.separator + gachaName + ".yml");
        createFileIfNotExists(file);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ArrayList<GachaItemStack> index = compressItemStack(itemStacks);
        String out = "";
        for(int i = 0;i < itemStacks.size();i++){
            out += index.indexOf(itemStacks.get(i)) + "," + itemStacks.get(i).amount + "|";
        }
        config.set("storage", out);
        for(int i = 0;i < index.size();i++){
            config.set("index." + i + ".data", new SItemStack(index.get(i).item).toBase64());
            config.set("index." + i + ".commands", index.get(i).commands);
            config.set("index." + i + ".giveItem", index.get(i).giveItem);
            config.set("index." + i + ".broadcastMessage", index.get(i).broadcastMessage);
            config.set("index." + i + ".playerMessage", index.get(i).playerMessage);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFileIfNotExists(File file){
        if(!file.exists()){
            file.mkdir();
        }
    }

    private ArrayList<GachaItemStack> compressItemStack(ArrayList<GachaItemStack> items){
        ArrayList<GachaItemStack> out = new ArrayList<>();
        for (GachaItemStack item : items) {
            if (!out.contains(item)) {
                out.add(item);
            }
        }
        return out;
    }

}
