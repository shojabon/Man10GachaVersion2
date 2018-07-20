package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.*;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemBankPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemStackPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaVaultPayment;
import com.shojabon.man10gachav2.enums.GachaPaymentType;
import com.shojabon.man10gachav2.enums.GachaSpinAlgorithm;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sho on 2018/06/23.
 */
public class GachaGame {
    private GachaSettings settings;
    private ArrayList<GachaPayment> payments;
    ArrayList<GachaItemStack> itemIndex;


    public GachaGame(String name){
        File file = new File(Bukkit.getPluginManager().getPlugin("Man10GachaV2").getDataFolder(), "gacha" + File.separator + name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        settings = new GachaSettings(getSettingsMap(config));
        payments = getPaymentList(config);
        itemIndex = getItemStackMap(config);
    }

    private Map<String, Object> getSettingsMap(FileConfiguration config){
        Map<String, Object> map = new HashMap<>();
        ConfigurationSection configurationSection = config.getConfigurationSection("settings");
        for(String keys: configurationSection.getKeys(false)){
            switch (keys) {
                case "sound":
                    Sound sound = Sound.valueOf(config.getString("settings.sound.sound"));
                    map.put("sound", new GachaSound(sound, Float.valueOf(config.getString("settings.sound.volume")), Float.valueOf(config.getString("settings.sound.pitch"))));
                    break;
                case "spinAlgorithm":
                    map.put("spinAlgorithm", GachaSpinAlgorithm.valueOf(config.getString("settings.spinAlgorithm")));
                    break;
                case "icon":
                    map.put("icon", new SItemStack(config.getString("settings.icon")).build());
                    break;
                default:
                    map.put(keys, config.get("settings." + keys));
                    break;
            }
        }
        return map;
    }

    private ArrayList<GachaPayment> getPaymentList(FileConfiguration config){
        ArrayList<GachaPayment> payments = new ArrayList<>();
        ConfigurationSection configurationSection = config.getConfigurationSection("payments");
        for(String numKeys: configurationSection.getKeys(false)){
            GachaPaymentType paymentType = GachaPaymentType.valueOf(config.getString("payments." + numKeys + ".type"));
            switch (paymentType){
                case VAULT:
                    payments.add(new GachaPayment(new GachaVaultPayment(config.getDouble("payments." + numKeys + ".value"))));
                    break;
                case ITEM:
                    payments.add(new GachaPayment(new GachaItemStackPayment(new SItemStack(config.getString("payments." + numKeys + ".item")).build(), config.getInt("amount"))));
                    break;
                case ITEM_BANK:
                    payments.add(new GachaPayment(new GachaItemBankPayment(config.getInt("payments." + numKeys + ".item"), config.getLong("config.payments."  +numKeys + ".amount"))));
                    break;
            }
        }
        return payments;
    }

    private ArrayList<GachaItemStack> getItemStackMap(FileConfiguration config){
        ConfigurationSection configurationSection = config.getConfigurationSection("index");
        ArrayList<GachaItemStack> index = new ArrayList<>();
        for(String numKey : configurationSection.getKeys(false)){
            Map<String, Object> map = new HashMap<>();
            for(String key: config.getConfigurationSection("index." + numKey).getKeys(false)){
                switch (key){
                    case "item":
                        map.put(key, new SItemStack(config.getString("index." + numKey + "." + key)));
                        break;
                    case "commands":
                        map.put(key, config.getStringList("index." + numKey + "." + key));
                        break;
                    case "broadcastMessage":
                        map.put(key, config.getStringList("index." + numKey + "." + key));
                        break;
                    case "playerMessage":
                        map.put(key, config.getStringList("index." + numKey + "." + key));
                        break;
                    case "giveItem":
                        map.put(key, config.getBoolean("index." + numKey + "." + key));
                        break;
                    case "playerTitleText":
                        map.put(key, new GachaTitleText(
                                config.getString("index." + numKey + "." + key + ".mainText"),
                                config.getString("index." + numKey + "." + key + ".subText"),
                                config.getInt("index." + numKey + "." + key + ".fadeInTime"),
                                config.getInt("index." + numKey + "." + key + ".time"),
                                config.getInt("index." + numKey + "." + key + ".fadeOutTime")
                        ));
                        break;
                    case "broadcastTitleText":
                        map.put(key, new GachaTitleText(
                                config.getString("index." + numKey + "." + key + ".mainText"),
                                config.getString("index." + numKey + "." + key + ".subText"),
                                config.getInt("index." + numKey + "." + key + ".fadeInTime"),
                                config.getInt("index." + numKey + "." + key + ".time"),
                                config.getInt("index." + numKey + "." + key + ".fadeOutTime")
                        ));
                        break;
                    case "items":
                        ArrayList<ItemStack> items = new ArrayList<>();
                        for(String itemData : config.getStringList("index." + numKey + "." + key)){
                            items.add(new SItemStack(itemData).build());
                        }
                        map.put(key, items);
                        break;
                    case "pexGroup":
                        map.put(key,  config.getStringList("index." + numKey + "." + key));
                        break;
                    case "pexPermission":
                        map.put(key,  config.getStringList("index." + numKey + "." + key));
                        break;
                    case "teleport":
                        map.put(key, new GachaTeleport(
                                config.getString("index." + numKey + "." + key + ".world"),
                                config.getDouble("index." + numKey + "." + key + ".x"),
                                config.getDouble("index." + numKey + "." + key + ".y"),
                                config.getDouble("index." + numKey + "." + key + ".z"),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".pitch")),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".yaw"))
                        ));
                        break;
                    case "broadcastSound":
                        map.put(key, new GachaSound(
                                Sound.valueOf(config.getString("index." + numKey + "." + key + ".sound")),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".volume")),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".pitch"))
                        ));
                        break;
                    case "playerSound":
                        map.put(key, new GachaSound(
                                Sound.valueOf(config.getString("index." + numKey + "." + key + ".sound")),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".volume")),
                                Float.valueOf(config.getString("index." + numKey + "." + key + ".pitch"))
                        ));
                        break;
                    case "playerPotionEffect":
                        ArrayList<GachaPotionEffect> effects = new ArrayList<>();
                        for(String numnumKey: config.getConfigurationSection("index." + numKey  +"." + key).getKeys(false)){
                            effects.add(new GachaPotionEffect(
                                    PotionEffectType.getByName(config.getString("index." + numKey + "." + key +"." + numnumKey + ".effect")),
                                    config.getInt("index." + numKey + "." + key +"." + numnumKey +  ".level"),
                                    config.getInt("index." + numKey + "." + key +"." + numnumKey + ".time")
                            ));
                        }
                        map.put(key, effects);
                        break;
                    case "broadcastPotionEffect":
                        ArrayList<GachaPotionEffect> effectss = new ArrayList<>();
                        for(String numnumKey: config.getConfigurationSection("index." + numKey  +"." + key).getKeys(false)){
                            effectss.add(new GachaPotionEffect(
                                    PotionEffectType.getByName(config.getString("index." + numKey + "." + key +"." + numnumKey + ".effect")),
                                    config.getInt("index." + numKey + "." + key +"." + numnumKey +  ".level"),
                                    config.getInt("index." + numKey + "." + key +"." + numnumKey + ".time")
                            ));
                        }
                        map.put(key, effectss);
                        break;
                    case "givePlayerItemBank":
                        ArrayList<GachaItemBankData> datas = new ArrayList<>();
                        for(String numnumKey: config.getConfigurationSection("index." + numKey  +"." + key).getKeys(false)){
                            datas.add(new GachaItemBankData(
                                    config.getInt("index." + numKey + "." + key + "." + numnumKey + ".itemType"),
                                    config.getLong("index." + numKey + "." + key + "." + numnumKey + ".itemAmount")
                            ));
                        }
                        map.put(key, datas);
                        break;
                    case "takePlayerItemBank":
                        ArrayList<GachaItemBankData> datass= new ArrayList<>();
                        for(String numnumKey: config.getConfigurationSection("index." + numKey  +"." + key).getKeys(false)){
                            datass.add(new GachaItemBankData(
                                    config.getInt("index." + numKey + "." + key + "." + numnumKey + ".itemType"),
                                    config.getLong("index." + numKey + "." + key + "." + numnumKey + ".itemAmount")
                            ));
                        }
                        map.put(key, datass);
                        break;
                    default:
                        map.put(key,  config.get("index." + numKey + "." + key));
                        break;
                }
            }
            index.add(new GachaItemStack(map));
        }
        return index;
    }
}
