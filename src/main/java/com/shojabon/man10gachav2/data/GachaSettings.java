package com.shojabon.man10gachav2.data;

import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.enums.GachaSpinAlgorithm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sho on 2018/07/12.
 */
public class GachaSettings {
    public String name;
    public String title = "Gacha";
    public int startDelay = 0;
    public GachaSound spinSound = new GachaSound(Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
    public float spinSpeed = 1;
    public GachaSpinAlgorithm spinAlgorithm = GachaSpinAlgorithm.RAMP;
    public boolean forceOpen = false;
    public boolean showPercentage = false;
    public boolean locked = false;
    public String usePermission;
    public ItemStack icon = new ItemStack(Material.DIAMOND);
    public long startOn = 0;
    public long endOn = 0;

    public GachaSettings(String name){
        this.name = name;
    }

    public GachaSettings(Map<String, Object> settings){
        for(String key: settings.keySet()){
            switch (key){
                case "name":
                    this.name = String.valueOf(settings.get(key));
                    break;
                case "title":
                    this.title = String.valueOf(settings.get(key));
                    break;
                case "forceOpen":
                    this.forceOpen = Boolean.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "showPercentage":
                    this.showPercentage = Boolean.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "locked":
                    this.locked= Boolean.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "startOn":
                    this.startOn = Long.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "endOn":
                    this.endOn = Long.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "startDelay":
                    this.startDelay = Integer.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "sound":
                    this.spinSound = (GachaSound) settings.get(key);
                    break;
                case "spinSpeed":
                    this.spinSpeed = Float.valueOf(String.valueOf(settings.get(key)));
                    break;
                case "spinAlgorithm":
                    this.spinAlgorithm = (GachaSpinAlgorithm) settings.get(key);
                    break;
                case "userPermission":
                    this.usePermission = String.valueOf(settings.get(key));
                    break;
                case "icon":
                    this.icon = (ItemStack) settings.get(key);
                    break;
            }
        }
    }

    public GachaSettings(String name,
                         String title,
                         int startDelay,
                         GachaSound spinSound,
                         float spinSpeed,
                         GachaSpinAlgorithm spinAlgorithm,
                         boolean forceOpen,
                         boolean showPercentage,
                         boolean locked,
                         String usePermission,
                         ItemStack icon,
                         long startOn,
                         long endOn){
        this.name = name;
        this.title = title;
        this.startDelay = startDelay;
        this.spinSound = spinSound;
        this.spinSpeed = spinSpeed;
        this.spinAlgorithm = spinAlgorithm;
        this.forceOpen = forceOpen;
        this.showPercentage = showPercentage;
        this.locked = locked;
        this.usePermission = usePermission;
        this.icon = icon;
        this.startOn = startOn;
        this.endOn = endOn;
    }

    public Map<String, Object> getStringData() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("title", this.title);
        if(forceOpen){
            map.put("forceOpen", true);
        }
        if(showPercentage){
            map.put("showPercentage", true);
        }
        map.put("locked", this.locked);
        if(startOn != 0 && startOn >= 1){
            map.put("startOn", this.startOn);
        }
        if(endOn != 0 && endOn>= 1){
            map.put("endOn", this.endOn);
        }
        if (startDelay != 0 && startDelay >= 1) {
            map.put("startDelay", this.startDelay);
        }
        if (spinSound != null) {
            map.put("sound", this.spinSound);
        } else {
            map.put("sound", new GachaSound(Sound.BLOCK_DISPENSER_DISPENSE, 1, 1));
        }
        if (spinSpeed < 1) {
            map.put("spinSpeed", 1);
        } else {
            map.put("spinSpeed", spinSpeed);
        }
        if (spinAlgorithm != null) {
            map.put("spinAlgorithm", spinAlgorithm);
        } else {
            map.put("spinAlgorithm", GachaSpinAlgorithm.RAMP);
        }
        if (usePermission != null) {
            map.put("usePermission", usePermission);
        }
        if (icon != null) {
            if(!icon.equals(new ItemStack(Material.DIAMOND))){
                map.put("icon", icon);
            }
        }
        return map;
    }


}