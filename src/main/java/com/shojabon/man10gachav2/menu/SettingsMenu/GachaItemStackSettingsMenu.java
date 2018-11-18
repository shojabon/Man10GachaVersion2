package com.shojabon.man10gachav2.menu.SettingsMenu;


import com.shojabon.man10gachav2.GachaGame;
import com.shojabon.man10gachav2.apis.*;
import com.shojabon.man10gachav2.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GachaItemStackSettingsMenu {
    GachaContainerSettingsMenu menu;
    Function<InventoryClickEvent, String> cancelFunction;
    GachaItemStack gItemStack;
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Man10GachaV2");
    int index;
    int amount;
    private ItemStack renderItemList(SItemStack itemStack, ArrayList<String> string){
        if(string == null){
            return itemStack.addLore("§b§l現在設定：なし").build();
        }
        for(String o : string){
            itemStack.addLore("§b§l" + o);
        }
        return itemStack.build();
    }

    public GachaItemStackSettingsMenu(GachaContainerSettingsMenu menu, int originalAmount, ItemStack item, Function<InventoryClickEvent, String> cancelFunction, int index){
        menu.p.closeInventory();
        this.index = index;
        this.menu = menu;
        this.amount = originalAmount;
        this.cancelFunction = cancelFunction;
        String itemString;
        if(item.getItemMeta() == null || item.getItemMeta().getLore() == null || !item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).contains("§m§a§n§1§0")) {
            itemString = null;
        }else{
            itemString = menu.decodeString(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1));
        }
        if(itemString != null) menu.decodeString(itemString.replaceAll("§m§a§n§1§0", ""));
        if(itemString != null){
            Bukkit.broadcastMessage("hasString:" + itemString);
            gItemStack = new GachaItemStack(menu.itemStackMap.get(itemString));
        }else{
            gItemStack = new GachaItemStack(item);
        }
        Bukkit.broadcastMessage(String.valueOf(gItemStack.getComparisonString()));
        createMenu(0, 0);
    }

    public void createMenu(int startCategory, int startPage){
        List<ItemStack> generalSettingsItem = new ArrayList<>();
        String name = gItemStack.item.getType().name();
        if(gItemStack.item.getItemMeta().getDisplayName() != null) name = gItemStack.item.getItemMeta().getDisplayName();
        generalSettingsItem.add(new SItemStack(Material.NAME_TAG).setDisplayname("§c§l§nアイテム設定").addLore("§b§l現在設定:" + name + " §b§l" + gItemStack.item.getAmount()).build());
        SItemStack items = new SItemStack(Material.NAME_TAG).setAmount(10).setDisplayname("§c§l§nアイテム複数排出設定");
        if(gItemStack.items != null){
            for(ItemStack item: gItemStack.items){
                String itemName = item.getType().name();
                if(item.getItemMeta().getDisplayName() != null) itemName = item.getItemMeta().getDisplayName();
                items.addLore("§b§l" + itemName + " §b§l" + item.getAmount());
            }
        }else{items.addLore("§b§l現在設定:なし");}
        generalSettingsItem.add(items.build());

        SItemStack commands = new SItemStack(Material.COMMAND).setDisplayname("§d§l§nコマンド設定");
        generalSettingsItem.add(renderItemList(commands, gItemStack.commands));

        generalSettingsItem.add(new SItemStack(Material.CHEST).setDisplayname("§6§l§nアイテム排出禁止設定").addLore("§b§l現在設定：" + gItemStack.giveItem).build());

        List<ItemStack> soundSettings = new ArrayList<>();
        if(gItemStack.playerSound != null) {
            Map<String, String> playerSoundMap = gItemStack.playerSound.getStringData();
            soundSettings.add(new SItemStack(Material.NOTE_BLOCK).setDisplayname("§d§l§nプレイヤー再生音声設定").addLore("§b§l音名:" + playerSoundMap.get("sound")).addLore("§b§lボリューム:" + playerSoundMap.get("volume")).addLore("§b§lピッチ:" + playerSoundMap.get("pitch")).build());
        }else{
            soundSettings.add(new SItemStack(Material.NOTE_BLOCK).setDisplayname("§d§l§nプレイヤー再生音声設定").addLore("§b§l現在設定：なし").build());
        }
        if(gItemStack.broadcastSound != null){
            Map<String, String> broadcastSoundMap = gItemStack.broadcastSound.getStringData();
            soundSettings.add(new SItemStack(Material.JUKEBOX).setDisplayname("§d§l§nサーバー再生音声設定").addLore("§b§l音名:" + broadcastSoundMap.get("sound")).addLore("§b§lボリューム:" + broadcastSoundMap.get("volume")).addLore("§b§lピッチ:" + broadcastSoundMap.get("pitch")).build());
        }else{
            soundSettings.add(new SItemStack(Material.JUKEBOX).setDisplayname("§d§l§nサーバー再生音声設定").addLore("§b§l現在設定：なし").build());
        }
        List<ItemStack> messageSettings = new ArrayList<>();
        SItemStack playerMessage = new SItemStack(Material.BOOK).setDisplayname("§6§l§nプレイヤーメッセージ設定");
        messageSettings.add(renderItemList(playerMessage, gItemStack.playerMessage));

        SItemStack broadcastMessage = new SItemStack(Material.BOOKSHELF).setDisplayname("§6§l§nサーバーメッセージ設定設定");
        messageSettings.add(renderItemList(broadcastMessage, gItemStack.broadcastMessage));


        List<ItemStack> permissionSettings = new ArrayList<>();
        permissionSettings.add(renderItemList(new SItemStack(Material.BARRIER).setDisplayname("§c§l§n付与権限設定"), gItemStack.pexPermission));
        permissionSettings.add(renderItemList(new SItemStack(Material.BARRIER).setAmount(10).setDisplayname("§c§l§n付与権限グループ設定"), gItemStack.pexGroup));

        List<ItemStack> vaultSettings = new ArrayList<>();
        vaultSettings.add(new SItemStack(Material.SKULL_ITEM).setDamage(3).setDisplayname("§a§l§nプレイヤーバランス払い出し設定").addLore("§b§l現在設定：" + gItemStack.givePlayerMoney).build());
        vaultSettings.add(new SItemStack(Material.SKULL_ITEM).setDamage(3).setDisplayname("§c§l§nプレイヤーバランス払い入れ設定").addLore("§b§l現在設定：" + gItemStack.takePlayerMoney).build());
        vaultSettings.add(new SItemStack(Material.FLOWER_POT_ITEM).setDisplayname("§a§l§nサーバー払い出し設定").addLore("§b§l現在設定：" + gItemStack.giveServerMoney).build());
        vaultSettings.add(new SItemStack(Material.FLOWER_POT_ITEM).setDisplayname("§c§l§nサーバー払い入れ設定").addLore("§b§l現在設定：" + gItemStack.takeServerMoney).build());

        List<ItemStack> itemBankSettings = new ArrayList<>();
        itemBankSettings.add(new SItemStack(Material.BARRIER).setDisplayname("§c§l§n近いうちのアップデートで追加されます").build());

        List<ItemStack> misc = new ArrayList<>();
        if(gItemStack.playerTitleText != null){
            Map<String, String> map = gItemStack.playerTitleText.getStringData();
            misc.add(new SItemStack(Material.SIGN).setDisplayname("§6§l§nプレイヤー表示タイトル設定").addLore("§b§lメインテキスト：" + map.get("mainText")).addLore("§b§lサブテキスト：" + map.get("subText")).addLore("§b§lフェードイン時間：" + map.get("fadeInTime")).addLore("§b§l表示時間：" + map.get("time")).addLore("§b§lフェードアウトタイム：" + map.get("fadeOutTime")).build());
        }else{misc.add(new SItemStack(Material.SIGN).setDisplayname("§6§l§nプレイヤー表示タイトル設定").addLore("§b§l現在設定：なし").build());}
        if(gItemStack.broadcastTitleText != null){
            Map<String, String> map = gItemStack.broadcastTitleText.getStringData();
            misc.add(new SItemStack(Material.SIGN).setDisplayname("§6§l§nサーバー表示タイトル設定").addLore("§b§lメインテキスト：" + map.get("mainText")).addLore("§b§lサブテキスト：" + map.get("subText")).addLore("§b§lフェードイン時間：" + map.get("fadeInTime")).addLore("§b§l表示時間：" + map.get("time")).addLore("§b§lフェードアウトタイム：" + map.get("fadeOutTime")).build());
        }else{misc.add(new SItemStack(Material.SIGN).setDisplayname("§6§l§nサーバー表示タイトル設定").addLore("§b§l現在設定：なし").build());}
        if(gItemStack.teleport != null){
            Map<String, String> map = gItemStack.teleport.getStringData();
            misc.add(new SItemStack(Material.COMPASS).setDisplayname("§7§l§nテレポート設定").addLore("§b§lワールド：" + map.get("world")).addLore("§b§lX：" + map.get("x")).addLore("§b§lY：" + map.get("y")).addLore("§b§lZ：" + map.get("z")).addLore("§b§lPitch：" + map.get("pitch")).addLore("§b§lYaw：" + map.get("yaw")).build());
        }else{misc.add(new SItemStack(Material.COMPASS).setDisplayname("§7§l§nテレポート設定").addLore("§b§l現在設定：なし").build());}
        misc.add(new SItemStack(Material.DIAMOND_SWORD).setDisplayname("§4§l§nプレイヤー殺害設定").addLore("§b§l現在設定：" + gItemStack.killPlayer).build());

        List<CategorizedMenuCategory> categories = new ArrayList<>();
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.DIAMOND).setDisplayname("§7§n§l主要設定").build(), generalSettingsItem));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.NOTE_BLOCK).setDisplayname("§a§n§lサ§b§n§lウ§c§n§lン§d§n§lド§e§n§l設§f§n§l定").build(), soundSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.BOOK_AND_QUILL).setDisplayname("§6§n§lメッセージ設定").build(), messageSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.BARRIER).setDisplayname("§c§n§l権限設定").build(), permissionSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.EMERALD).setDisplayname("§e§n§lVault設定").build(), vaultSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.STORAGE_MINECART).setDisplayname("§8§n§lアイテムバンク設定").build(), itemBankSettings));
        categories.add(new CategorizedMenuCategory(new SItemStack(Material.LAVA_BUCKET).setDisplayname("§b§n§lその他の設定").build(), misc));
        new CategorizedMenuAPI("", menu.p, categories, (event, categorizedMenuLocation) -> {
            onClick(categorizedMenuLocation.getCategory(), categorizedMenuLocation.getNum());
            return null;
        }, cancelFunction, startCategory, startPage);
    }

    private void reopenMenu(int startCategory, int startPage){
        new BukkitRunnable(){

            @Override
            public void run() {
                createMenu(startCategory, startPage);
            }
        }.runTaskLater(plugin, 1);
    }

    private void onClick(int category, int slot){
        switch (category){
            case 0: {
                generalSettings(slot);
                break;
            }
            case 1:{
                soundSettings(slot);
                break;
            }
            case 2:{
                messageSettings(slot);
                break;
            }
            case 3:{
                permissionSettings(slot);
                break;
            }
            case 4:{
                vaultSettings(slot);
                break;
            }
            case 5:{
                //itembank
                break;
            }
            case 6:{
                miscSettings(slot);
            }
        }
    }

    private void pushSettings(){
        menu.itemStackMap.put(gItemStack.getComparisonString(), gItemStack);
        Bukkit.broadcastMessage(gItemStack.getComparisonString());
        menu.items.put(index, new GachaFinalItemStack(gItemStack, amount));
    }

    private void generalSettings(int slot){
        Player p = menu.p;
        switch (slot){
            case 0:{
                //アイテム設定
                new ItemStackSelectorAPI("§b§lアイテムを選択してください", p, gItemStack.item, (event, itemStack) -> {
                    gItemStack.item = itemStack;
                    pushSettings();
                    reopenMenu(0,0);
                    return null;
                }, event -> {
                    reopenMenu(0,0);
                    return null;
                });
                break;
            }
            case 1:{
                //アイテム複数排出設定
                new MultiItemStackSelectorAPI(p, gItemStack.items, (event, itemStacks) -> {
                    if(itemStacks.size() == 0){
                        gItemStack.items = null;
                    }else{
                        gItemStack.items = itemStacks;
                    }
                    pushSettings();
                    reopenMenu(0,0);
                    return null;
                }, event -> {
                    reopenMenu(0,0);
                    return null;
                });
                break;
            }
            case 2:{
                //コマンド設定
                new StringListEditorAPI(p, gItemStack.commands, (strings, player) -> {
                    gItemStack.commands = new ArrayList<>(strings);
                    pushSettings();
                    reopenMenu(0,0);
                    return null;
                }, strings -> {
                    reopenMenu(0,0);
                    return null;
                });
                break;
            }
            case 3:{
                //アイテム排出設定
                new BooleanSelectorAPI("§b§lタイトル", p, new ItemStack(Material.CHEST), gItemStack.giveItem, (event, aBoolean) -> {
                    gItemStack.giveItem = aBoolean;
                    pushSettings();
                    reopenMenu(0,0);
                    return null;
                }, event -> {
                    reopenMenu(0,0);
                    return null;
                });
                break;
            }
        }
    }

    private void soundSettings(int slot){
        switch (slot){
            case 0:{
                //プレイヤー再生音設定
                GachaSound sound = gItemStack.playerSound;
                new SoundSelectorAPI("§b§lプレイヤー再生音を選択してください", menu.p, sound.getVolume(), sound.getPitch(), sound.getSound(), (event, gachaSound) -> {
                    gItemStack.playerSound = gachaSound;
                    pushSettings();
                    reopenMenu(1, 0);
                    return null;
                }, event -> {
                    reopenMenu(1, 0);
                    return null;
                });
                break;
            }
            case 1:{
                //サーバー再生音設定
                GachaSound sound = gItemStack.playerSound;
                new SoundSelectorAPI("§b§lサーバー再生音を選択してください", menu.p, sound.getVolume(), sound.getPitch(), sound.getSound(), (event, gachaSound) -> {
                    gItemStack.playerSound = gachaSound;
                    pushSettings();
                    reopenMenu(1, 0);
                    return null;
                }, event -> {
                    reopenMenu(1, 0);
                    return null;
                });
                break;
            }
        }
    }

    private void messageSettings(int slot){
        switch (slot){
            case 0:{
                //プレイヤーメッセージ設定
                new StringListEditorAPI(menu.p, gItemStack.commands, (strings, player) -> {
                    gItemStack.playerMessage = new ArrayList<>(strings);
                    pushSettings();
                    reopenMenu(2,0);
                    return null;
                }, strings -> {
                    reopenMenu(2,0);
                    return null;
                });
                break;
            }
            case 1:{
                //サーバーメッセージ設定
                new StringListEditorAPI(menu.p, gItemStack.commands, (strings, player) -> {
                    gItemStack.broadcastMessage = new ArrayList<>(strings);
                    pushSettings();
                    reopenMenu(2,0);
                    return null;
                }, strings -> {
                    reopenMenu(2,0);
                    return null;
                });
                break;
            }
        }
    }

    private void permissionSettings(int slot){
        switch (slot){
            case 0:{
                //付与権限設定
                new StringListEditorAPI(menu.p, gItemStack.commands, (strings, player) -> {
                    gItemStack.pexPermission = new ArrayList<>(strings);
                    pushSettings();
                    reopenMenu(3,0);
                    return null;
                }, strings -> {
                    reopenMenu(3,0);
                    return null;
                });
                break;
            }
            case 1:{
                //付与権限グループ設定
                new StringListEditorAPI(menu.p, gItemStack.commands, (strings, player) -> {
                    gItemStack.pexGroup = new ArrayList<>(strings);
                    pushSettings();
                    reopenMenu(3,0);
                    return null;
                }, strings -> {
                    reopenMenu(3,0);
                    return null;
                });
                break;
            }
        }
    }

    private void vaultSettings(int slot){
        switch (slot){
            case 0:{
                //プレイヤー払い出し
                new NumberInputAPI("§b§lプレイヤー支払額を入力してください", menu.p, 9,(event, integer) -> {
                    gItemStack.givePlayerMoney = integer;
                    pushSettings();
                    reopenMenu(4,0);
                    return null;
                }, event -> {
                    reopenMenu(4,0);
                    return null;
                });
                break;
            }
            case 1:{
                //プレイヤー払い入れ
                new NumberInputAPI("§b§lプレイヤー払い入れ額を入力してください", menu.p, 9,(event, integer) -> {
                    gItemStack.takePlayerMoney = integer;
                    pushSettings();
                    reopenMenu(4,0);
                    return null;
                }, event -> {
                    reopenMenu(4,0);
                    return null;
                });
                break;
            }
            case 2:{
                //サーバー払い出し
                new NumberInputAPI("§b§lサーバー支払額を入力してください", menu.p, 9,(event, integer) -> {
                    gItemStack.giveServerMoney = integer;
                    pushSettings();
                    reopenMenu(4,0);
                    return null;
                }, event -> {
                    reopenMenu(4,0);
                    return null;
                });
                break;
            }
            case 3:{
                //サーバー払い入れ
                new NumberInputAPI("§b§lサーバー払い入れ額を入力してください", menu.p, 9,(event, integer) -> {
                    gItemStack.takeServerMoney = integer;
                    pushSettings();
                    reopenMenu(4,0);
                    return null;
                }, event -> {
                    reopenMenu(4,0);
                    return null;
                });
                break;
            }
        }
    }

    private void itemBankSettings(int slot){
    }

    private void miscSettings(int slot){
        switch (slot){
            case 0:{
                //プレイヤー表示タイトル設定
                new TitleTextSelectorAPI(menu.p, "§b§lプレイヤー表示タイトル設定", gItemStack.playerTitleText,  (event, titleText) -> {
                    gItemStack.playerTitleText = titleText;
                    pushSettings();
                    reopenMenu(6, 0);
                    return null;
                }, event -> {
                    reopenMenu(6, 0);
                    return null;
                });
                break;
            }
            case 1:{
                //サーバー表示タイトル設定
                new TitleTextSelectorAPI(menu.p, "§b§lサーバー表示タイトル設定", gItemStack.playerTitleText,  (event, titleText) -> {
                    gItemStack.broadcastTitleText = titleText;
                    pushSettings();
                    reopenMenu(6, 0);
                    return null;
                }, event -> {
                    reopenMenu(6, 0);
                    return null;
                });
                break;
            }
            case 2:{
                //テレポート設定
                new LocationSelectorAPI("§b§lテレポート設定", gItemStack.teleport, menu.p, (event, location) -> {
                    if(location != null) gItemStack.teleport = new GachaTeleport(location);
                    pushSettings();
                    reopenMenu(6,0);
                    return null;
                }, event -> {
                    reopenMenu(6,0);
                    return null;
                });
                break;
            }
            case 3:{
                //殺害設定
                new BooleanSelectorAPI("§b§lプレイヤー殺害設定", menu.p, new ItemStack(Material.DIAMOND_SWORD), gItemStack.killPlayer, (event, aBoolean) -> {
                    gItemStack.killPlayer = aBoolean;
                    pushSettings();
                    reopenMenu(6,0);
                    return null;
                }, event -> {
                    reopenMenu(6,0);
                    return null;
                });
                break;
            }
        }
    }



}
