package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sho on 2018/06/23.
 */
public class Man10GachaAPI {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Man10GachaV2");

    public void createNewGacha(GachaSettings gachaSettings ,ArrayList<GachaPayment> payments, ArrayList<GachaFinalItemStack> itemStacks){
        File file = new File(plugin.getDataFolder(), "gacha" + File.separator + gachaSettings.name + ".yml");
        createFileIfNotExists(file);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, Object> settingsMap = gachaSettings.getStringData();
        printSettings(settingsMap, config);

        printPayment(payments, config);


        ArrayList<GachaItemStack> index = compressItemStack(itemStacks);
        String out = "";
        for(int i = 0;i < itemStacks.size();i++){
            out += index.indexOf(itemStacks.get(i).getItemStack()) + "," + itemStacks.get(i).getAmount()+ "|";
        }
        config.set("storage", out);
        for(int i = 0;i < index.size();i++){
            Map<String, Object> item = index.get(i).getStringData();
            printItemIndex(item, config, i);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void printPayment(ArrayList<GachaPayment> payments, FileConfiguration config){
        for(int i = 0;i < payments.size();i++){
            Map<String, String> data = payments.get(i).getStringData();
            for(String key: data.keySet()){
                config.set("payments." + i + "." + key, data.get(key));
            }
        }
    }


    private void printSettings(Map<String, Object> settings, FileConfiguration config){
        for(String key : settings.keySet()) {
            if (key.equals("sound")) {
                Map<String, String> soundMap = ((GachaSound) settings.get(key)).getStringData();
                config.set("settings." + key + ".sound", soundMap.get("sound"));
                config.set("settings." + key + ".volume", soundMap.get("volume"));
                config.set("settings." + key + ".pitch", soundMap.get("pitch"));
            }else if(key.equals("icon")){
                config.set("settings." + key, new SItemStack((ItemStack) settings.get(key)).setAmount(1).toBase64());
            }else if(key.equals("spinAlgorithm")){
                config.set("settings." + key, settings.get("spinAlgorithm").toString());
            }else{
                config.set("settings." + key, settings.get(key));
            }
        }
    }


    private void printItemIndex(Map<String, Object> itemData, FileConfiguration config, int id){
        for(String key: itemData.keySet()){
            if(key.equals("commands") || key.equals("broadcastMessage") || key.equals("playerMessage") || key.equals("pexGroup") || key.equals("pexPermission")){
                config.set("index." + id +"." + key, itemData.get(key));
            }else if(key.equals("items")){
                ArrayList<String> out = new ArrayList<>();
                ArrayList<ItemStack> items = (ArrayList<ItemStack>) itemData.get(key);
                for(int i = 0;i < items.size();i++){
                    out.add(new SItemStack(items.get(i)).toBase64());
                }
                config.set("index." + id +"." + key, out);
            }else if(key.equals("playerTitleText") || key.equals("broadcastTitleText")){
                GachaTitleText text = (GachaTitleText) itemData.get(key);
                Map<String, String> map = text.getStringData();
                config.set("index." + id +"." + key + ".mainText", map.get("mainText"));
                config.set("index." + id +"." + key + ".subText", map.get("subText"));
                config.set("index." + id +"." + key + ".fadeInTime", map.get("fadeInTime"));
                config.set("index." + id +"." + key + ".time", map.get("time"));
                config.set("index." + id +"." + key + ".fadeOutTime", map.get("fadeOutTime"));
            } else if(key.equals("teleport")){
                Map<String, String> map = ((GachaTeleport) itemData.get(key)).getStringData();
                config.set("index." + id +"." + key + ".world", map.get("world"));
                config.set("index." + id +"." + key + ".x", map.get("x"));
                config.set("index." + id +"." + key + ".y", map.get("y"));
                config.set("index." + id +"." + key + ".z", map.get("z"));
                config.set("index." + id +"." + key + ".pitch", map.get("pitch"));
                config.set("index." + id +"." + key + ".yaw", map.get("yaw"));
            }else if(key.equals("broadcastSound") || key.equals("playerSound")){
                Map<String, String> map = ((GachaSound) itemData.get(key)).getStringData();
                config.set("index." + id +"." + key + ".sound", map.get("sound"));
                config.set("index." + id +"." + key + ".volume", map.get("volume"));
                config.set("index." + id +"." + key + ".pitch", map.get("pitch"));
            }else if(key.equals("playerPotionEffect") || key.equals("broadcastPotionEffect")){
                ArrayList<GachaPotionEffect> gachaPotionList = (ArrayList<GachaPotionEffect>) itemData.get(key);
                for(int i = 0;i < gachaPotionList.size();i++){
                    Map<String, String> map = gachaPotionList.get(i).getStringData();
                    config.set("index." + id +"." + key + "." + i + ".effect", map.get("effect"));
                    config.set("index." + id +"." + key + "." + i + ".level", map.get("level"));
                    config.set("index." + id +"." + key + "." + i + ".time", map.get("time"));
                }
            }else if(key.equals("givePlayerItemBank") || key.equals("takePlayerItemBank")){
                ArrayList<GachaItemBankData> gachaItemBankData = (ArrayList<GachaItemBankData>) itemData.get(key);
                for(int i = 0;i < gachaItemBankData.size();i++){
                    Map<String, String> map = gachaItemBankData.get(i).getStringData();
                    config.set("index." + id +"." + key + "." + i + ".itemType", map.get("itemType"));
                    config.set("index." + id +"." + key + "." + i + ".itemAmount", map.get("itemAmount"));
                }
            }else{
                config.set("index." + id + "." + key, itemData.get(key));
            }
        }
    }

    private void createFileIfNotExists(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<GachaItemStack> compressItemStack(ArrayList<GachaFinalItemStack> items){
        ArrayList<GachaItemStack> out = new ArrayList<>();
        for (GachaFinalItemStack item : items) {
            if (!out.contains(item.getItemStack())) {
                out.add(item.getItemStack());
            }
        }
        return out;
    }

}
