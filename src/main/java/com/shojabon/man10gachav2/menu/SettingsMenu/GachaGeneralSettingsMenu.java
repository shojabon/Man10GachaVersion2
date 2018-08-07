package com.shojabon.man10gachav2.menu.SettingsMenu;

import com.shojabon.man10gachav2.GachaGame;
import com.shojabon.man10gachav2.Man10GachaAPI;
import com.shojabon.man10gachav2.apis.*;
import com.shojabon.man10gachav2.data.CategorizedMenuCategory;
import com.shojabon.man10gachav2.data.GachaSound;
import com.shojabon.man10gachav2.enums.GachaSettingsIcon;
import com.shojabon.man10gachav2.menu.GachaSettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GachaGeneralSettingsMenu {
    String gacha;
    Player p;
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Man10GachaV2");
    Man10GachaAPI api;
    String prefix = "§6[§aMg§fac§dha§5V2§6]§f";
    GachaGame game;
    public GachaGeneralSettingsMenu(String gacha, Player p){
        this.gacha = gacha;
        this.p = p;
        api = new Man10GachaAPI();
        game =  Man10GachaAPI.gachaGameMap.get(gacha);
    }
    public void createMenu(int startCategory, int startPage){
        p.closeInventory();
        game = Man10GachaAPI.gachaGameMap.get(gacha);
        List<ItemStack> generalSettingsItem = new ArrayList<>();
        generalSettingsItem.add(new SItemStack(Material.NAME_TAG).setDisplayname("§c§l§nガチャの登録名設定").addLore("§b§l現在設定:" + gacha).build());
        generalSettingsItem.add(new SItemStack(Material.ANVIL).setDisplayname("§c§l§nガチャのタイトル設定").addLore("§b§l現在設定:" + game.getSettings().title.replaceAll("&", "§")).build());
        generalSettingsItem.add(new SItemStack(Material.PAINTING).setDisplayname("§c§l§nアイコン設定").addLore("§b§l現在設定:" + game.getSettings().icon.getType().name()).build());
        generalSettingsItem.add(new SItemStack(Material.COMPASS).setDisplayname("§c§l§n回転スピード設定").addLore("§b§l現在設定:" + game.getSettings().spinSpeed).build());
        generalSettingsItem.add(new SItemStack(Material.DISPENSER).setDisplayname("§c§l§n回転アルゴリズム設定").addLore("§b§l現在設定:" + game.getSettings().spinAlgorithm.name()).build());

        List<ItemStack> soundSettings = new ArrayList<>();
        Map<String, String> soundMap = game.getSettings().spinSound.getStringData();
        soundSettings.add(new SItemStack(Material.NOTE_BLOCK).setDisplayname("§c§l§n回転音設定").addLore("§b§l音名:" + soundMap.get("sound")).addLore("§b§lボリューム:" + soundMap.get("volume")).addLore("§b§lピッチ:" + soundMap.get("pitch")).build());

        List<ItemStack> misc = new ArrayList<>();
        misc.add(new SItemStack(Material.WATCH).setDisplayname("§c§l§n開始ディレイ設定").addLore("§b§l現在設定:" + game.getSettings().startDelay).build());
        misc.add(new SItemStack(Material.IRON_DOOR).setDisplayname("§c§l§n強制メニューオープン設定").addLore("§b§l現在設定:" + game.getSettings().forceOpen).build());
        //misc.add(new SItemStack(Material.COMPASS).setDisplayname("§c§l§n確率表示設定").addLore("§b§l現在設定:" + game.getSettings().showPercentage).build());


        List<CategorizedMenuCategory> categories = new ArrayList<>();
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.DIAMOND).setDisplayname("§7§n§l主要設定").build(), generalSettingsItem));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.NOTE_BLOCK).setDisplayname("§a§n§lサ§b§n§lウ§c§n§lン§d§n§lド§e§n§l設§f§n§l定").build(), soundSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.LAVA_BUCKET).setDisplayname("§b§n§lその他の設定").build(), misc));

        new CategorizedMenuAPI("§f§l" + gacha + "の一般設定", p, categories, (event, categorizedMenuLocation) -> {
            p.closeInventory();
            settings(categorizedMenuLocation.getCategory(), categorizedMenuLocation.getNum());
            return null;
        }, event -> {
            new GachaSettingsMenu(gacha, p);
            return null;
        }, startCategory, startPage);
    }

    private void pushSettings(){
        api.printSettings(gacha, game.getSettings());
        api.reloadGacha(gacha);
    }

    private void settings(int category, int id){
        switch (category){
            case 0: {
                generalCategory(id);
                return;
            }
            case 1:{
                soundGategory(id);
                return;
            }
            case 2:{
                miscCategory(id);
                return;
            }
        }
    }

    private void restartMenu(int startCategory, int startPage){
        new BukkitRunnable(){

            @Override
            public void run() {
                createMenu(startCategory, startPage);
            }
        }.runTaskLater(plugin, 1);
    }

    private void generalCategory(int id){
        switch(id){
            case 0: {
                //登録名変更設定
                new AnvilGUI(p, gacha, (event, s) -> {
                    if (event == null) {
                        restartMenu(0,0);
                        return null;
                    }
                    if (s == null) {
                        p.sendMessage(prefix + "§c§lガチャ名に空白は使えません");
                        return "restart";
                    }
                    if (gacha.equalsIgnoreCase(s)) {
                        p.sendMessage(prefix + "§c§l変更予定名が現在名と同じです");
                        restartMenu(0, 0);
                        return null;
                    }
                    int n = api.renameGacha(gacha, s);
                    if (n == -1) {
                        p.sendMessage(prefix + "§c§lガチャが存在しません");
                    }
                    if (n == -2) {
                        p.sendMessage(prefix + "§c§l変更予定名のガチャがすでに存在します");
                    }
                    if (n == -3) {
                        p.sendMessage(prefix + "§c§l内部的エラーが発生しました");
                    }
                    if (n == 0) {
                        gacha = s;
                    }
                    restartMenu(0,0);
                    return null;
                });
                break;
            }
            case 1: {
                //タイトル設定
                new LongTextInputAPI(p, "§5§lガチャのタイトルを入力してください", (player, s) -> {
                    game.getSettings().title = s;
                    pushSettings();
                    restartMenu(0,1);
                    return null;
                }, player -> {
                    restartMenu(0,1);
                    return null;
                });
                break;
            }
            case 2: {
                //アイコン設定
                new ItemStackSelectorAPI("§b§lのアイコンを選択してください", p, game.getSettings().icon, (event, itemStack) -> {
                    if(itemStack == game.getSettings().icon){
                        p.sendMessage(prefix + "§c§l過去と同じアイコンは使用できません");
                        return "restart";
                    }
                    game.getSettings().icon = itemStack;
                    int i = api.printSettings(gacha, game.getSettings());
                    if(i == -1){
                        p.sendMessage(prefix + "§c§lガチャが存在しません");
                        restartMenu(0,0);
                        return null;
                    }
                    if(i == -2){
                        p.sendMessage(prefix + "§c§l内部的エラーが発生しました");
                        restartMenu(0,0);
                        return null;
                    }
                    if(i == 0){
                        api.reloadGacha(gacha);
                        restartMenu(0,0);
                        return null;
                    }
                    return null;
                }, event -> {
                    restartMenu(0,0);
                    return null;
                });
                break;
            }
            case 3: {
                //回転速度設定
                new NumberInputAPI("§b§l回転速度を入力してください", p, 3, (event, integer) -> {
                    if (integer == 0) {
                        p.sendMessage(prefix + "§c§l回転速度は1以上を入力してください");
                        return "restart";
                    }
                    if (integer > 200) {
                        p.sendMessage(prefix + "§c§l回転速度は200以下を入力してください");
                        return "restart";
                    }
                    game.getSettings().spinSpeed = integer;
                    pushSettings();
                    restartMenu(0,0);
                    return null;
                }, event -> {
                    restartMenu(0,0);
                    return null;
                });
            }
        }
    }

    private void soundGategory(int id){
        switch (id){
            case 0:{
                //回転音設定
                GachaSound sound = game.getSettings().spinSound;
                new SoundSelectorAPI("§b§l回転音を設定してください", p, sound.getVolume(), sound.getPitch(), sound.getSound(), (event, gachaSound) -> {
                    game.getSettings().spinSound = gachaSound;
                    pushSettings();
                    restartMenu(1,0);
                    return null;
                }, event -> {
                    restartMenu(1, 0);
                    return null;
                });
            }
        }
    }

    private void miscCategory(int id){
        switch (id){
            case 0:{
                new NumberInputAPI("§b§l開始ディレイを入力してください(秒)", p, 3, (event, integer) -> {
                    game.getSettings().startDelay = integer;
                    pushSettings();
                    restartMenu(2, 0);
                    return null;
                }, event -> {
                    restartMenu(2,0);
                    return null;
                });
                return;
            }
            case 1: {
                new BooleanSelectorAPI("§b§l強制メニューオープン設定", p, new ItemStack(Material.IRON_DOOR), game.getSettings().forceOpen, (event, aBoolean) -> {
                    game.getSettings().forceOpen = aBoolean;
                    pushSettings();
                    restartMenu(2, 0);
                    return null;
                }, event -> {
                    restartMenu(2, 0);
                    return null;
                });
                return;
            }
            case 2: {
                new BooleanSelectorAPI("§b§l確率表示設定", p, new ItemStack(Material.IRON_DOOR), game.getSettings().showPercentage, (event, aBoolean) -> {
                    game.getSettings().showPercentage = aBoolean;
                    pushSettings();
                    restartMenu(2, 0);
                    return null;
                }, event -> {
                    restartMenu(2, 0);
                    return null;
                });
                return;
            }
        }
    }
}
