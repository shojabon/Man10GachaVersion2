package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.GachaItemBank;
import com.shojabon.man10gachav2.apis.GachaVault;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.*;
import com.shojabon.man10gachav2.enums.GachaPaymentType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by sho on 2018/06/23.
 */
public class Man10GachaAPI {
    private Plugin plugin = Bukkit.getPluginManager().getPlugin("Man10GachaV2");
    public static HashMap<String, GachaGame> gachaGameMap = new HashMap<>();
    static HashMap<UUID, GachaGameStateData> playerGameStateMap = new HashMap<>();
    static HashMap<Location, GachaSignData> signDataMap = new HashMap<>();
    private GachaItemBank itembank;
    private GachaVault vault;


    public List<String> getGachasInDirectory(){
        List<String> name = new ArrayList<>();
        File folder = new File(plugin.getDataFolder(), File.separator + "gacha");
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            name.add(deleteExtention(listOfFile.getName()));
        }
        return name;
    }

    private String deleteExtention(String fileName){
        String fname = fileName;
        int pos = fname.lastIndexOf(".");
        if (pos > 0) {
            fname = fname.substring(0, pos);
        }
        return fname;
    }

    public boolean ifGachaLocked(String name){
        return getGacha(name).getSettings().locked;
    }

    public String getSignGacha(Location l){
        if(!ifGachaSign(l)){
            return null;
        }
        return signDataMap.get(l).getGacha();
    }

    public void registerNewSign(GachaSignData data){
        createSignsFileIfNotExist();
        resetSignFile();
        signDataMap.put(data.getLocation(), data);
        wrightSignsFile();
    }

    public void deleteSign(Location location){
        createSignsFileIfNotExist();
        resetSignFile();
        signDataMap.remove(location);
        wrightSignsFile();
    }

    public void loadSignFile(){
        File f = new File(plugin.getDataFolder(), "signs.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        signDataMap.clear();
        for(String key : config.getKeys(false)){
            GachaSignData data = new GachaSignData(new Location(Bukkit.getWorld(config.getString(key + ".world")), config.getInt(key + ".x"), config.getInt(key + ".y"), config.getInt(key + ".z")), config.getString(key + ".gacha"));
            signDataMap.put(data.getLocation(), data);
        }
    }

    public void resetSignFile(){
        File f = new File(plugin.getDataFolder(), "signs.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        for(String key: config.getKeys(false)){
            config.set(key, null);
        }
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wrightSignsFile(){
        createSignsFileIfNotExist();
        resetSignFile();
        File f = new File(plugin.getDataFolder(), "signs.yml");
        Configuration signConfig = YamlConfiguration.loadConfiguration(f);
        for(int i = 0;i < signDataMap.keySet().size();i++){
            GachaSignData data = signDataMap.get(signDataMap.keySet().toArray()[i]);
            signConfig.set(i + ".gacha", data.getGacha());
            signConfig.set(i + ".x", data.getLocation().getBlockX());
            signConfig.set(i + ".y", data.getLocation().getBlockY());
            signConfig.set(i + ".z", data.getLocation().getBlockZ());
            signConfig.set(i + ".world", data.getLocation().getWorld().getName());
        }
        try {
            ((YamlConfiguration) signConfig).save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Man10GachaAPI(){
        itembank = new GachaItemBank();
        vault = new GachaVault();
    }

    public GachaGame getGacha(String name){
        if(!gachaGameMap.containsKey(name)){
            GachaGame game = new GachaGame(name, (JavaPlugin) plugin);
            gachaGameMap.put(name, game);
        }
        return gachaGameMap.get(name);
    }

    public int renameGacha(String oldGacha, String newGacha){
        File oldFile = new File(Bukkit.getPluginManager().getPlugin("Man10GachaV2").getDataFolder(), "gacha" + File.separator + oldGacha + ".yml");
        if(!oldFile.exists()){
            return -1;
        }
        File newFile = new File(Bukkit.getPluginManager().getPlugin("Man10GachaV2").getDataFolder(), "gacha" + File.separator + newGacha + ".yml");
        if(newFile.exists()){
            return -2;
        }
        boolean success = oldFile.renameTo(newFile);
        if(!success){
            return -3;
        }
        gachaGameMap.remove(oldGacha);
        getGacha(newGacha);
        return 0;
    }


    public boolean ifGachaSign(Location l){
        return signDataMap.containsKey(l);
    }

    public boolean ifPlayerHasEnoughForPayment(Player p, GachaGame game){
        if(game == null){
            return false;
        }
        ArrayList<GachaPayment> payments = game.getGachaPayments();
        HashMap<ItemStack, Integer> inventoryMap = createInventoryItemMap(p);
        for(GachaPayment payment : payments){
            if(payment.getType() == GachaPaymentType.VAULT){
                if(!vault.hasEnough(p.getUniqueId(), payment.getVaultPayment().getValue())) return false;
            }
            if(payment.getType() == GachaPaymentType.ITEM){
                ItemStack itemSearchingFor = payment.getItemStackPayment().getItemStack();
                itemSearchingFor.setAmount(1);
                if(!inventoryMap.containsKey(itemSearchingFor)) return false;
                if(payment.getItemStackPayment().getAmount() >= inventoryMap.get(itemSearchingFor)) return false;
            }
            if(payment.getType() == GachaPaymentType.ITEM_BANK){
                if(!itembank.hasEnough(p.getUniqueId(), payment.getItemBankPayment().getId(), payment.getItemBankPayment().getAmount())) return false;
            }
        }
        return true;
    }

    public boolean takePayment(Player p, GachaGame game){
        if(game == null){
            return false;
        }
        if(!ifPlayerHasEnoughForPayment(p, game)){
            return false;
        }
        ArrayList<GachaPayment> payments = game.getGachaPayments();
        HashMap<ItemStack, Integer> inventoryMap = createInventoryItemMap(p);
        for(GachaPayment payment : payments){
            if(payment.getType() == GachaPaymentType.VAULT){
                vault.takeMoney(p.getUniqueId(), payment.getVaultPayment().getValue());
            }else if(payment.getType() == GachaPaymentType.ITEM){
                ItemStack itemSearchingFor = payment.getItemStackPayment().getItemStack().clone();
                itemSearchingFor.setAmount(1);
                if(!inventoryMap.containsKey(itemSearchingFor)) return false;
                if(payment.getItemStackPayment().getAmount() >= inventoryMap.get(itemSearchingFor)) return false;
            }else if(payment.getType() == GachaPaymentType.ITEM_BANK){
                itembank.takeItems(p.getUniqueId(), payment.getItemBankPayment().getId(), payment.getItemBankPayment().getAmount());
            }
        }
        return true;
    }

    public boolean takeItemFromInventory(Player p, ItemStack item, int amount){
        item.setAmount(1);
        for(ItemStack items : p.getInventory().getContents()){
            if(items != null){
                int amo = items.getAmount();
                items.setAmount(1);
                if(item == items){
                    if(amo >= amount){
                        items.setAmount(amo - amount);
                        amount = 0;
                    }else {
                        items.setType(Material.AIR);
                        amount = amount - amo;
                    }
                }
            }
        }
        return true;
    }

    public List<String> getLackingPaymentMessage(Player p, GachaGame game){
        if(game == null){
            return null;
        }
        List<String> messages = new ArrayList<>();
        ArrayList<GachaPayment> payments = game.getGachaPayments();
        HashMap<ItemStack, Integer> inventoryMap = createInventoryItemMap(p);
        for(GachaPayment payment : payments){
            if(payment.getType() == GachaPaymentType.VAULT){
                if(vault.hasEnough(p.getUniqueId(), payment.getVaultPayment().getValue())) {
                    messages.add("§2§l✔ §7§l| §a§l現金 " + payment.getVaultPayment().getValue() + "円");
                }else{
                    messages.add("§4§l✖ §7§l| §c§l現金 " + payment.getVaultPayment().getValue() + "円");
                }
            }
            if(payment.getType() == GachaPaymentType.ITEM){
                ItemStack itemSearchingFor = payment.getItemStackPayment().getItemStack();
                itemSearchingFor.setAmount(1);
                String name = payment.getItemStackPayment().getItemStack().getItemMeta().getDisplayName();
                if(name == null) name = payment.getItemStackPayment().getItemStack().getType().name();
                if(inventoryMap.containsKey(itemSearchingFor) || payment.getItemStackPayment().getAmount() <= inventoryMap.get(itemSearchingFor)){
                    messages.add("§2§l✔ §7§l| §a§lアイテム 『" + name + "§a§l』が" + payment.getItemStackPayment().getAmount() + "個");
                }else{
                    messages.add("§4§l✖ §7§l| §4§lアイテム 『" + name + "§4§l』が" + payment.getItemStackPayment().getAmount() + "個");
                }
            }
            if(payment.getType() == GachaPaymentType.ITEM_BANK){
                if(itembank.hasEnough(p.getUniqueId(), payment.getItemBankPayment().getId(), payment.getItemBankPayment().getAmount())){
                    messages.add("§2§l✔ §7§l| §a§lアイテムバンク 『" + itembank.getName(payment.getItemBankPayment().getId()) + "§a§l』が" + payment.getItemStackPayment().getAmount() + "個");
                }else{
                    messages.add("§4§l✖ §7§l| §4§lアイテムバンク 『" + itembank.getName(payment.getItemBankPayment().getId()) + "§4§l』が" + payment.getItemStackPayment().getAmount() + "個");
                }
            }
        }
        return messages;
    }

    public List<String> getPayMessage(Player p, GachaGame game){
        if(game == null){
            return null;
        }
        List<String> messages = new ArrayList<>();
        ArrayList<GachaPayment> payments = game.getGachaPayments();
        HashMap<ItemStack, Integer> inventoryMap = createInventoryItemMap(p);
        for(GachaPayment payment : payments){
            if(payment.getType() == GachaPaymentType.VAULT){
                p.sendMessage("§a§l現金§7§l|§a§l" + NumberFormat.getNumberInstance(Locale.US).format(payment.getVaultPayment().getValue()) + "円");
            }
            if(payment.getType() == GachaPaymentType.ITEM){
                String name = payment.getItemStackPayment().getItemStack().getItemMeta().getDisplayName();
                if(name == null) name = payment.getItemStackPayment().getItemStack().getType().name();
                p.sendMessage("§a§lアイテム§7§l|§a§l『" + name + "§a§l』を " + NumberFormat.getNumberInstance(Locale.US).format(payment.getItemStackPayment().getAmount()) +"個");
            }
            if(payment.getType() == GachaPaymentType.ITEM_BANK){
                p.sendMessage("§a§lアイテムバンク§7§l|§a§l『" + itembank.getName(payment.getItemBankPayment().getId()) + "§a§l』を" + NumberFormat.getNumberInstance(Locale.US).format(payment.getItemBankPayment().getAmount()) + "個");
            }
        }
        return messages;
    }


    public boolean ifGachaExists(String name){
        if(gachaGameMap.containsKey(name)){
            return true;
        }
        File file = new File(plugin.getDataFolder(), "gacha" + File.separator + name + ".yml");
        return file.exists();
    }

    public void createNewGacha(GachaSettings gachaSettings ,ArrayList<GachaPayment> payments, ArrayList<GachaFinalItemStack> itemStacks){
        File file = new File(plugin.getDataFolder(), "gacha" + File.separator + gachaSettings.name + ".yml");
        createFileIfNotExists(file);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, Object> settingsMap = gachaSettings.getStringData();
        printSettings(settingsMap, config);

        printPayment(payments, config);


        ArrayList<GachaItemStack> index = compressItemStack(itemStacks);
        StringBuilder out = new StringBuilder();
        for (GachaFinalItemStack itemStack : itemStacks) {
            out.append(index.indexOf(itemStack.getItemStack())).append(",").append(itemStack.getAmount()).append("|");
        }
        config.set("storage", out.toString().substring(0, toString().length() -1));
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

    public void createNewGacha(GachaGame game){
        createNewGacha(game.getSettings(), game.getGachaPayments(), game.getItemStacks());
    }

    public void createSignsFileIfNotExist(){
        File f = new File(plugin.getDataFolder(), "signs.yml");
        if(f.exists()){
            return;
        }
        try {
            boolean b = f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean ifSignsFileExists(){
        return new File(plugin.getDataFolder(), "signs.yml").exists();
    }

    private HashMap<ItemStack, Integer> createInventoryItemMap(Player p){
        HashMap<ItemStack, Integer> map = new HashMap<>();
        for(ItemStack item : p.getInventory().getContents()){
            if(item != null){

                int amount = item.getAmount();
                item.clone().setAmount(1);
                if(map.containsKey(item)){
                    map.put(item, map.get(item) + amount);
                }else{
                    map.put(item, amount);
                }
            }
        }
        return map;
    }

    public int printSettings(String gacha, GachaSettings settings){
        File file = new File(plugin.getDataFolder(), "gacha" + File.separator + gacha + ".yml");
        if(!file.exists()){
            return -1;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("settings", null);
        printSettings(settings.getStringData(), config);
        try {
            config.save(file);
        } catch (IOException e) {
            return -2;
        }
        return 0;
    }

    public void reloadGacha(String gacha){
        gachaGameMap.remove(gacha);
        getGacha(gacha);
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
            switch (key) {
                case "sound":
                    Map<String, String> soundMap = ((GachaSound) settings.get(key)).getStringData();
                    config.set("settings." + key + ".sound", soundMap.get("sound"));
                    config.set("settings." + key + ".volume", soundMap.get("volume"));
                    config.set("settings." + key + ".pitch", soundMap.get("pitch"));
                    break;
                case "icon":
                    config.set("settings." + key, new SItemStack((ItemStack) settings.get(key)).setAmount(1).toBase64());
                    break;
                case "spinAlgorithm":
                    config.set("settings." + key, settings.get("spinAlgorithm").toString());
                    break;
                default:
                    config.set("settings." + key, settings.get(key));
                    break;
            }
        }
    }

    private void printItemIndex(Map<String, Object> itemData, FileConfiguration config, int id){
        for(String key: itemData.keySet()){
            switch (key) {
                case "commands":
                case "broadcastMessage":
                case "playerMessage":
                case "pexGroup":
                case "pexPermission":
                    config.set("index." + id + "." + key, itemData.get(key));
                    break;
                case "items":
                    ArrayList<String> out = new ArrayList<>();
                    ArrayList<ItemStack> items = (ArrayList<ItemStack>) itemData.get(key);
                    for (ItemStack item : items) {
                        out.add(new SItemStack(item).toBase64());
                    }
                    config.set("index." + id + "." + key, out);
                    break;
                case "playerTitleText":
                case "broadcastTitleText": {
                    GachaTitleText text = (GachaTitleText) itemData.get(key);
                    Map<String, String> map = text.getStringData();
                    config.set("index." + id + "." + key + ".mainText", map.get("mainText"));
                    config.set("index." + id + "." + key + ".subText", map.get("subText"));
                    config.set("index." + id + "." + key + ".fadeInTime", map.get("fadeInTime"));
                    config.set("index." + id + "." + key + ".time", map.get("time"));
                    config.set("index." + id + "." + key + ".fadeOutTime", map.get("fadeOutTime"));
                    break;
                }
                case "teleport": {
                    Map<String, String> map = ((GachaTeleport) itemData.get(key)).getStringData();
                    config.set("index." + id + "." + key + ".world", map.get("world"));
                    config.set("index." + id + "." + key + ".x", map.get("x"));
                    config.set("index." + id + "." + key + ".y", map.get("y"));
                    config.set("index." + id + "." + key + ".z", map.get("z"));
                    config.set("index." + id + "." + key + ".pitch", map.get("pitch"));
                    config.set("index." + id + "." + key + ".yaw", map.get("yaw"));
                    break;
                }
                case "broadcastSound":
                case "playerSound": {
                    Map<String, String> map = ((GachaSound) itemData.get(key)).getStringData();
                    config.set("index." + id + "." + key + ".sound", map.get("sound"));
                    config.set("index." + id + "." + key + ".volume", map.get("volume"));
                    config.set("index." + id + "." + key + ".pitch", map.get("pitch"));
                    break;
                }
                case "playerPotionEffect":
                case "broadcastPotionEffect":
                    ArrayList<GachaPotionEffect> gachaPotionList = (ArrayList<GachaPotionEffect>) itemData.get(key);
                    for (int i = 0; i < gachaPotionList.size(); i++) {
                        Map<String, String> map = gachaPotionList.get(i).getStringData();
                        config.set("index." + id + "." + key + "." + i + ".effect", map.get("effect"));
                        config.set("index." + id + "." + key + "." + i + ".level", map.get("level"));
                        config.set("index." + id + "." + key + "." + i + ".time", map.get("time"));
                    }
                    break;
                case "givePlayerItemBank":
                case "takePlayerItemBank":
                    ArrayList<GachaItemBankData> gachaItemBankData = (ArrayList<GachaItemBankData>) itemData.get(key);
                    for (int i = 0; i < gachaItemBankData.size(); i++) {
                        Map<String, String> map = gachaItemBankData.get(i).getStringData();
                        config.set("index." + id + "." + key + "." + i + ".itemType", map.get("itemType"));
                        config.set("index." + id + "." + key + "." + i + ".itemAmount", map.get("itemAmount"));
                    }
                    break;
                default:
                    config.set("index." + id + "." + key, itemData.get(key));
                    break;
            }
        }
    }

    private void createFileIfNotExists(File file){
        if(!file.exists()){
            try {
                boolean b = file.createNewFile();
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
