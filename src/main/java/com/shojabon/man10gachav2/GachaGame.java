package com.shojabon.man10gachav2;

import com.shojabon.man10gachav2.GachaAlgorithm.GachaSpinAlgorithmManager;
import com.shojabon.man10gachav2.apis.GachaItemBank;
import com.shojabon.man10gachav2.apis.GachaVault;
import com.shojabon.man10gachav2.apis.SInventory;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.*;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemBankPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemStackPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaVaultPayment;
import com.shojabon.man10gachav2.enums.GachaGameState;
import com.shojabon.man10gachav2.enums.GachaPaymentType;
import com.shojabon.man10gachav2.enums.GachaSpinAlgorithm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

/**
 * Created by sho on 2018/06/23.
 */
public class GachaGame {
    private GachaSettings settings;
    private ArrayList<GachaPayment> payments;
    private ArrayList<GachaItemStack> itemIndex;
    private Inventory gameInventory;
    private Listener listener = new Listener();
    private ArrayList<GachaFinalItemStack> itemStacks;
    private HashMap<UUID, Inventory> inventoryMap = new HashMap<>();
    private HashMap<UUID, ArrayList<Integer>> indexMap = new HashMap<>();
    private JavaPlugin plugin;
    private GachaVault vault;
    private GachaItemBank itemBank;

    public ArrayList<GachaPayment> getGachaPayments(){
        return payments;
    }

    public GachaSettings getSettings(){
        return settings;
    }

    public ArrayList<GachaItemStack> getItemIndex(){
        return itemIndex;
    }

    public ArrayList<GachaFinalItemStack> getItemStacks(){
        return itemStacks;
    }

    public GachaGame(String name, JavaPlugin plugin){
        this.plugin = plugin;
        File file = new File(Bukkit.getPluginManager().getPlugin("Man10GachaV2").getDataFolder(), "gacha" + File.separator + name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        settings = new GachaSettings(getSettingsMap(config));
        itemIndex = getItemStackMap(config);
        payments = getPaymentList(config);
        gameInventory = createDefaultInventory(settings.title);
        itemStacks = getItemStacks(config);
        Bukkit.getPluginManager().registerEvents(listener, this.plugin);
        vault = new GachaVault();
        itemBank = new GachaItemBank();
    }

    private Inventory createDefaultInventory(String title){
        SInventory inv = new SInventory(3, title);
        inv.setItem(new int[]{0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(7).setDisplayname("").build()).
        setItem(new int[]{4, 22}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("").build());
        return inv.build();
    }

    private ArrayList<GachaFinalItemStack> getItemStacks(FileConfiguration config){
        String items = config.getString("storage");
        ArrayList<GachaFinalItemStack> itemStacks = new ArrayList<>();
        String[] split = items.split("\\|");
        for(String s : split){
            String[] split1 = s.split(",");
            itemStacks.add(new GachaFinalItemStack(itemIndex.get(Integer.valueOf(split1[0])), Integer.valueOf(split1[1])));
        }
        return itemStacks;
    }

    public void play(Player p){
        if(settings.locked)return;
        if(Man10GachaAPI.playerGameStateMap.containsKey(p.getUniqueId())) return;
        Inventory inv = gameInventory;
        inventoryMap.put(p.getUniqueId(), inv);
        ArrayList<Integer> ints = new ArrayList<>();
        for(int i = 0;i < 18;i++){
            ints.add(0);
        }
        indexMap.put(p.getUniqueId(), ints);
        Man10GachaAPI.playerGameStateMap.put(p.getUniqueId(), new GachaGameStateData(p.getUniqueId(), GachaGameState.ROLLING, settings.name));
        p.openInventory(inv);
        Runnable r = () -> {
            double spinSpeed = (1000/settings.spinSpeed);
            while (true){
                roll(p.getUniqueId());
                settings.spinSound.playSoundToPlayer(p);
                placeItem(p.getUniqueId());
                try {
                    Thread.sleep((long) spinSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spinSpeed = new GachaSpinAlgorithmManager(settings.spinAlgorithm, (long) settings.spinSpeed, 1000/spinSpeed).update();
                if(spinSpeed >= 1900 || spinSpeed <= 0){
                    Man10GachaAPI.playerGameStateMap.put(p.getUniqueId(), new GachaGameStateData(p.getUniqueId(), GachaGameState.STOPPED, settings.name));
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            win(itemIndex.get(indexMap.get(p.getUniqueId()).get(13)), p, inventoryMap.get(p.getUniqueId()).getItem(13).getAmount());
            indexMap.remove(p.getUniqueId());
            Man10GachaAPI.playerGameStateMap.remove(p.getUniqueId());
            inventoryMap.remove(p.getUniqueId());
            p.closeInventory();
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void win(GachaItemStack item, Player p, int amount){
        if(item.playerSound != null){
            item.playerSound.playSoundToPlayer(p);
        }
        if(item.broadcastSound != null){
            item.broadcastSound.playSoundToServerExeptPlayer(p);
        }
        if(item.giveItem){
            ItemStack payoutItem = item.item;
            payoutItem.setAmount(amount);
            p.getInventory().addItem(payoutItem);
        }
        if(item.commands != null){
            for(String s : item.commands){
                String displayName = item.item.getItemMeta().getDisplayName();
                if(displayName == null){
                    displayName = item.item.getType().name();
                }
                String command = s.replaceAll("%TITLE%", settings.title).replaceAll("&", "§").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("%UUID%", String.valueOf(p.getUniqueId()))
                        .replaceAll("%ITEM%", displayName).replace("%AMOUNT%", String.valueOf(amount));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
        if(item.broadcastMessage != null){
            for(String s : item.broadcastMessage){
                String displayName = item.item.getItemMeta().getDisplayName();
                if(displayName == null){
                    displayName = item.item.getType().name();
                }
                String command = s.replaceAll("%TITLE%", settings.title).replaceAll("&", "§").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("%UUID%", String.valueOf(p.getUniqueId()))
                        .replaceAll("%ITEM%", displayName).replace("%AMOUNT%", String.valueOf(amount));
                Bukkit.broadcastMessage(command);
            }
        }else{
            String displayName = item.item.getItemMeta().getDisplayName();
            if(displayName == null){
                displayName = item.item.getType().name();
            }
            p.sendMessage("§e§lおめでとうございます『" + displayName + "』が当たりました");
        }
        if(item.playerMessage != null){
            for(String s : item.playerMessage){
                String displayName = item.item.getItemMeta().getDisplayName();
                if(displayName == null){
                    displayName = item.item.getType().name();
                }
                String command = s.replaceAll("%TITLE%", settings.title).replaceAll("&", "§").replaceAll("%PLAYER%", p.getDisplayName()).replaceAll("%UUID%", String.valueOf(p.getUniqueId()))
                        .replaceAll("%ITEM%", displayName).replace("%AMOUNT%", String.valueOf(amount));
                p.sendMessage(command);
            }
        }
        if(item.playerTitleText != null){
            item.playerTitleText.playTitleToPlayer(p);
        }
        if(item.broadcastTitleText != null){
            item.broadcastTitleText.playTitleToPlayer(p);
        }
        if(item.items != null){
            for(ItemStack items : item.items){
                p.getInventory().addItem(items);
            }
        }
        if(item.teleport != null){
            item.teleport.teleportPlayerToLocation(p);
        }
        if(item.broadcastSound != null){
            item.broadcastSound.playSoundToServerExeptPlayer(p);
        }
        if(item.playerSound != null){
            item.playerSound.playSoundToPlayer(p);
        }
        if(item.playerPotionEffect != null){
            for(GachaPotionEffect effect : item.playerPotionEffect){
                effect.effectPlayer(p);
            }
        }
        if(item.broadcastPotionEffect != null){
            for(GachaPotionEffect effect : item.broadcastPotionEffect){
                effect.effectServerExeptPlayer(p);
            }
        }
        if(item.givePlayerMoney != 0){
            vault.giveMoney(p.getUniqueId(), item.givePlayerMoney);
        }
        if(item.takePlayerMoney != 0){
            vault.takeMoney(p.getUniqueId(), item.takePlayerMoney);
        }
        if(item.takeServerMoney != 0){
            for(Player pp: Bukkit.getServer().getOnlinePlayers()){
                vault.takeMoney(pp.getUniqueId(), item.takeServerMoney);
            }
        }
        if(item.giveServerMoney != 0){
            for(Player pp: Bukkit.getServer().getOnlinePlayers()){
                vault.giveMoney(pp.getUniqueId(), item.giveServerMoney);
            }
        }
        if(item.givePlayerItemBank != null){
            for(GachaItemBankData data : item.givePlayerItemBank){
                itemBank.giveItems(p.getUniqueId(), data.getId(), data.getAmount());
            }
        }
        if(item.takePlayerItemBank != null){
            for(GachaItemBankData data : item.takePlayerItemBank){
                itemBank.takeItems(p.getUniqueId(), data.getId(), data.getAmount());
            }
        }
        if(item.killPlayer){
            p.setHealth(0D);
        }
    }

    private void placeItem(UUID uuid){
        int rand = new Random().nextInt(itemStacks.size());
        GachaFinalItemStack finalItemStack = itemStacks.get(rand);
        ItemStack item = finalItemStack.getItemStack().item;
        item.setAmount(finalItemStack.getAmount());
        inventoryMap.get(uuid).setItem(17, item);
        indexMap.get(uuid).set(17, rand);
    }

    private void roll(UUID uuid){
        for(int i = 9;i < 17;i++){
            inventoryMap.get(uuid).setItem(i, inventoryMap.get(uuid).getItem(i + 1));
            indexMap.get(uuid).set(i, indexMap.get(uuid).get(i + 1));
        }
    }

    private class Listener implements org.bukkit.event.Listener {

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(!inventoryMap.containsKey(e.getWhoClicked().getUniqueId())){
                return;
            }
            e.setCancelled(true);
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(settings.forceOpen){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        e.getPlayer().openInventory(inventoryMap.get(e.getPlayer().getUniqueId()));
                    }
                }.runTaskLater(plugin, 5);
                return;
            }
        }
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
                case "spinSpeed":
                    int speed = config.getInt("settings.spinSpeed");
                    if(speed > 1000){
                        speed = 1000;
                    }
                    if(speed < 1){
                        speed = 1;
                    }
                    map.put(keys, speed);
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
                    double amount = Double.parseDouble(config.getString("payments." + numKeys + ".amount"));
                    payments.add(new GachaPayment(new GachaVaultPayment(amount)));
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
                        map.put(key, new SItemStack(config.getString("index." + numKey + "." + key)).build());
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
