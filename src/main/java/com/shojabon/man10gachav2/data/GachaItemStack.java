package com.shojabon.man10gachav2.data;

import com.google.gson.Gson;
import com.shojabon.man10gachav2.apis.SItemStack;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import static org.bukkit.Effect.Type.SOUND;

/**
 * Created by sho on 2018/06/23.
 */
public class GachaItemStack implements Serializable {
    public ItemStack item;
    public ArrayList<String> commands = null;
    public ArrayList<String> broadcastMessage = null;
    public ArrayList<String> playerMessage = null;

    public boolean giveItem = true;
    public GachaTitleText playerTitleText = new GachaTitleText();
    public GachaTitleText broadcastTitleText = new GachaTitleText();
    public ArrayList<ItemStack> items = null;
    public ArrayList<String> pexGroup = null;
    public ArrayList<String> pexPermission = null;

    public GachaTeleport teleport = null;
    public GachaSound broadcastSound = new GachaSound();
    public GachaSound playerSound = new GachaSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
    public ArrayList<GachaPotionEffect> playerPotionEffect = null;
    public ArrayList<GachaPotionEffect> broadcastPotionEffect = null;

    public long givePlayerMoney = 0;
    public long takePlayerMoney= 0;
    public long giveServerMoney= 0;
    public long takeServerMoney= 0;

    public ArrayList<GachaItemBankData> givePlayerItemBank = null;
    public ArrayList<GachaItemBankData> takePlayerItemBank = null;

    public boolean killPlayer = false;


    public GachaItemStack(ItemStack item){
        this.item = item;
    }

    public GachaItemStack(GachaItemStack itemStack){
        loadMap(itemStack.getStringData());
    }

    public GachaItemStack(Map<String, Object> settings){
        loadMap(settings);
    }

    private void loadMap(Map<String, Object> settings){
        for(String key: settings.keySet()){
            switch(key){
                case "item":
                    item = new SItemStack(String.valueOf(settings.get(key))).build();
                    break;
                case "commands":
                    commands = ((ArrayList<String>) settings.get(key));
                    break;
                case "broadcastMessage":
                    broadcastMessage = ((ArrayList<String>) settings.get(key));
                    break;
                case "playerMessage":
                    playerMessage = ((ArrayList<String>) settings.get(key));
                    break;
                case "giveItem":
                    giveItem = Boolean.getBoolean(String.valueOf(settings.get(key)));
                    break;
                case "playerTitleText":
                    playerTitleText = ((GachaTitleText) settings.get(key));
                    break;
                case "broadcastTitleText":
                    broadcastTitleText = ((GachaTitleText) settings.get(key));
                    break;
                case "items":
                    ArrayList<String> list = ((ArrayList<String>) settings.get(key));
                    items = new ArrayList<>();
                    for(String st : list){
                        items.add(new SItemStack(st).build());
                    }
                    break;
                case "pexGroup":
                    pexGroup = ((ArrayList<String>) settings.get(key));
                    break;
                case "pexPermission":
                    pexPermission = ((ArrayList<String>) settings.get(key));
                    break;
                case "teleport":
                    teleport = ((GachaTeleport) settings.get(key));
                    break;
                case "broadcastSound":
                    broadcastSound = ((GachaSound) settings.get(key));
                    break;
                case "playerSound":
                    playerSound = ((GachaSound) settings.get(key));
                    break;
                case "playerPotionEffect":
                    playerPotionEffect = ((ArrayList<GachaPotionEffect>)settings.get(key));
                    break;
                case "broadcastPotionEffect":
                    broadcastPotionEffect = ((ArrayList<GachaPotionEffect>)settings.get(key));
                    break;
                case "givePlayerItemBank":
                    givePlayerItemBank = ((ArrayList<GachaItemBankData>) settings.get(key));
                    break;
                case "takePlayerItemBank":
                    takePlayerItemBank = ((ArrayList<GachaItemBankData>) settings.get(key));
                    break;
                case "givePlayerMoney":
                    givePlayerMoney = Long.parseLong(String.valueOf(settings.get(key)));
                    break;
                case "takePlayerMoney":
                    takePlayerMoney = Long.parseLong(String.valueOf(settings.get(key)));
                    break;
                case "giveServerMoney":
                    giveServerMoney = Long.parseLong(String.valueOf(settings.get(key)));
                    break;
                case "takeServerMoney":
                    takeServerMoney = Long.parseLong(String.valueOf(settings.get(key)));
                    break;
                case "killPlayer":
                    killPlayer = Boolean.getBoolean(String.valueOf(settings.get(key)));
                    break;
            }
        }
    }

    public boolean isTheSame(GachaItemStack itemStack){
        return serialize(this).equals(serialize(itemStack));
    }

    private static String serialize(Object object) {
        ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = null;
        try {
            try {
                gzipOut = new GZIPOutputStream(new Base64OutputStream(byteaOut));
                gzipOut.write(new Gson().toJson(object).getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            if (gzipOut != null) try { gzipOut.close(); } catch (IOException ignored) {}
        }
        return new String(byteaOut.toByteArray());
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String getComparisonString(){
        Map<String, Object> map = getStringData();
        if(map.containsKey("teleport")) map.put("teleport", ((GachaTeleport)map.get("teleport")).getStringData());
        return getMD5(serialize(map));
    }


    public GachaItemStack(ItemStack item,
                          ArrayList<ItemStack> outputItems,
                          ArrayList<String> commands,
                          boolean giveItem,
                          ArrayList<String> broadcastMessage,
                          ArrayList<String> playerMessage,
                          GachaTitleText playerTitleText,
                          GachaTitleText broadcastTitleText,
                          ArrayList<String> pexGroup,
                          ArrayList<String> pexPermission,
                          GachaTeleport teleport,
                          GachaSound broadcastSound,
                          GachaSound playerSound,
                          ArrayList<GachaPotionEffect> playerPotionEffect,
                          ArrayList<GachaPotionEffect> broadcastPotionEffect,
                          long givePlayerMoney,
                          long giveServerMoney,
                          long takePlayerMoney,
                          long takeServerMoney,
                          ArrayList<GachaItemBankData> givePlayerItemBank,
                          ArrayList<GachaItemBankData> takePlayerItemBank,
                          boolean killPlayer


    ){
        this.item = item;
        this.commands = commands;
        this.broadcastMessage = broadcastMessage;
        this.playerMessage = playerMessage;
        this.giveItem = giveItem;
        this.playerTitleText = playerTitleText;
        this.broadcastTitleText = broadcastTitleText;
        this.items = outputItems;
        this.pexGroup = pexGroup;
        this.pexPermission = pexPermission;
        this.teleport = teleport;
        this.broadcastSound = broadcastSound;
        this.playerSound = playerSound;
        this.playerPotionEffect = playerPotionEffect;
        this.broadcastPotionEffect = broadcastPotionEffect;
        this.givePlayerMoney = givePlayerMoney;
        this.giveServerMoney = giveServerMoney;
        this.takePlayerMoney = takePlayerMoney;
        this.takeServerMoney = takeServerMoney;
        this.givePlayerItemBank = givePlayerItemBank;
        this.takePlayerItemBank = takePlayerItemBank;
        this.killPlayer = killPlayer;
    }

    public Map<String, Object> getStringData(){
        Map<String, Object> objects = new HashMap<>();
        objects.put("item", new SItemStack(this.item).setAmount(1).toBase64());
        if(commands != null){
            objects.put("commands", this.commands);
        }
        if(broadcastMessage != null){
            objects.put("broadcastMessage", this.broadcastMessage);
        }
        if(playerMessage != null){
            objects.put("playerMessage", this.playerMessage);
        }
        if(!giveItem){
            objects.put("giveItem", false);
        }
        if(playerTitleText != null){
            objects.put("playerTitleText", this.playerTitleText);
        }
        if(broadcastTitleText != null){
            objects.put("broadcastTitleText", this.broadcastTitleText);
        }
        if(items != null){
            ArrayList<String> items = new ArrayList<>();
            for(ItemStack item: this.items){
                items.add(new SItemStack(item).toBase64());
            }
            objects.put("items", items);
        }
        if(pexGroup != null){
            objects.put("pexGroup", this.pexGroup);
        }
        if(pexPermission != null){
            objects.put("pexPermission", this.pexPermission);
        }
        if(teleport != null){
            objects.put("teleport", this.teleport);
        }
        if(broadcastSound.usable()){
            objects.put("broadcastSound", this.broadcastSound);
        }
        if(playerSound != new GachaSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1)){
            objects.put("playerSound", this.playerSound);
        }
        if(playerPotionEffect != null){
            objects.put("playerPotionEffect", this.playerPotionEffect);
        }
        if(broadcastPotionEffect != null){
            objects.put("broadcastPotionEffect", this.broadcastPotionEffect);
        }
        if(givePlayerMoney != 0){
            objects.put("givePlayerMoney", this.givePlayerMoney);
        }
        if(takePlayerMoney != 0){
            objects.put("takePlayerMoney", this.takePlayerMoney);
        }
        if(giveServerMoney != 0){
            objects.put("giveServerMoney", this.giveServerMoney);
        }
        if(takeServerMoney != 0){
            objects.put("takeServerMoney", this.takeServerMoney);
        }
        if(givePlayerItemBank != null){
            objects.put("givePlayerItemBank", this.givePlayerItemBank);
        }
        if(takePlayerItemBank != null){
            objects.put("takePlayerItemBank", this.takePlayerItemBank);
        }
        if(killPlayer){
            objects.put("killPlayer", true);
        }
        return objects;
    }

}
