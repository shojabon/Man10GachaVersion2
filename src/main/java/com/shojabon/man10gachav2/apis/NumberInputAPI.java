package com.shojabon.man10gachav2.apis;

import com.google.common.collect.ForwardingMultimap;
import com.shojabon.man10gachav2.data.GachaBannerDictionary;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by sho on 2018/07/12.
 */
public class NumberInputAPI {

    private JavaPlugin plugin;
    private Player player;
    private int numberOfChars;
    private BiFunction<InventoryClickEvent, Integer, String> function;
    private String title;
    private Listener listener = null;
    private GachaBannerDictionaryLight bdl = new GachaBannerDictionaryLight();

    public NumberInputAPI(JavaPlugin plugin, String title, Player player, int numberOfChars, BiFunction<InventoryClickEvent, Integer, String> function){
        this.plugin = plugin;
        this.player = player;
        this.function = function;
        this.title = title;
        if(numberOfChars >= 9){
            numberOfChars = 9;
        }
        if(numberOfChars <= 1){
            numberOfChars = 1;
        }
        this.listener = new Listener(player.getUniqueId(), numberOfChars);
        this.numberOfChars = numberOfChars;
        SInventoryLight inv = new SInventoryLight(6, title);
                inv.fillInventory(new SItemStackLight(Material.STAINED_GLASS_PANE).setDamage(11).setDisplayname(" ").build()).setItem(new int[]{41, 42,50,51}, new SItemStackLight(Material.EMERALD_BLOCK).setDisplayname("§a§l§nConfirm").build()).
                setItem(new int[]{43, 44, 52, 53}, new SItemStackLight(Material.REDSTONE_BLOCK).setDisplayname("§c§l§nCancel").build());
        inv.setItem(48, new SItemStackLight(Material.TNT).setDisplayname("§c§lClear").build());
        inv.setItem(new int[]{0,1,2,3,4,5,6,7,8}, new ItemStack(Material.AIR));
        int redTilesToPlace = 9 - numberOfChars;
        for(int i = 0;i < redTilesToPlace;i++){
            inv.setItem(i, new SItemStackLight(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname(" ").build());
        }
        inv.setItem(new int[]{12, 15}, new SItemStackLight(Material.STAINED_GLASS_PANE).setDamage(11).setGlowingEffect(true).setDisplayname("§a§l,").build());
        inv.setItem(46, bdl.getItem(0));
        inv.setItem(37, bdl.getItem(1));
        inv.setItem(38, bdl.getItem(2));
        inv.setItem(39, bdl.getItem(3));
        inv.setItem(28, bdl.getItem(4));
        inv.setItem(29, bdl.getItem(5));
        inv.setItem(30, bdl.getItem(6));
        inv.setItem(19, bdl.getItem(7));
        inv.setItem(20, bdl.getItem(8));
        inv.setItem(21, bdl.getItem(9));
        Bukkit.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
        player.openInventory(inv.build());
    }

    public void closeInventory(){
        this.player.closeInventory();
        HandlerList.unregisterAll(this.listener);
    }

    class Listener implements org.bukkit.event.Listener{
        private UUID uuid;
        private String inputVal = "";
        private int maxChars;

        public Listener(UUID playerUUID, int maxChars){
            this.uuid = playerUUID;
            this.maxChars = maxChars;
        }

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(this.uuid != e.getWhoClicked().getUniqueId()){
                return;
            }
            int s = e.getSlot();
            e.setCancelled(true);
            if(s == 48) inputVal = "";
            if(s == 43 || s == 44 || s == 52 || s == 53){
                closeInventory();
                new Thread(function.apply(e, -1)).start();
                return;
            }
            if(s == 41 || s == 42 || s == 50 || s == 51){
                if(inputVal.equals("")){
                    closeInventory();
                    new Thread(function.apply(e, -2)).start();
                    return;
                }else{
                    closeInventory();
                    new Thread(function.apply(e, Integer.valueOf(inputVal))).start();
                    return;
                }
            }
            if(inputVal.length() == maxChars){
                return;
            }
            if(s == 46){
                if(inputVal.length() == 0){
                    return;
                }
                inputVal += "0";
            }
            if(s == 37) inputVal += "1";
            if(s == 38) inputVal += "2";
            if(s == 39) inputVal += "3";
            if(s == 28) inputVal += "4";
            if(s == 29) inputVal += "5";
            if(s == 30) inputVal += "6";
            if(s == 19) inputVal += "7";
            if(s == 20) inputVal += "8";
            if(s == 21) inputVal += "9";
            renderDisplay(e.getInventory());
        }

        @EventHandler
        public void onCLose(InventoryCloseEvent e){
            closeInventory();
        }

        void renderDisplay(Inventory inv){
            int[] ints = new int[inputVal.length()];
            for(int i = 0;i < ints.length;i++){
                ints[i] = Integer.parseInt(String.valueOf(inputVal.charAt(i)));
            }
            int startFrom = 9 - ints.length;
            int startFromClear = 9 - maxChars;
            for(int i = 0;i < maxChars;i++){
                inv.setItem(startFromClear + i, new ItemStack(Material.AIR));
            }
            for(int i = 0;i < ints.length;i++){
               inv.setItem(startFrom + i, bdl.getItem(ints[i]));
            }
        }
    }

}

class SItemStackLight {

    private ItemStack item = null;
    private int amount = 1;
    private String displayName = null;
    private List<String> lore = new ArrayList<>();
    private List<SEnchant> enchantments = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();
    private int damage = 0;
    private boolean isGlowing = false;
    private boolean isUnbreakable = false;
    private boolean skullMode = false;

    //skull builder part ↓

    private String url = null;
    private String skullOwner = null;

    /*
    Created By Sho in 2017/08/16 in Osaka
     */

    public SItemStackLight(Material material) {
        item = new ItemStack(material);
    }

    public SItemStackLight(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            convertItemStackToSItemStackLight(item);
        } catch (Exception e) {
        }
    }

    public SItemStackLight(ItemStack item) {
        convertItemStackToSItemStackLight(item);
    }

    private class SEnchant {
        public SEnchant(Enchantment ench, int leve) {
            this.ench = ench;
            this.level = leve;
        }

        Enchantment ench;
        int level;
    }

    public String toBase64() throws IllegalStateException {
        try {
            ItemStack item = this.build();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(1);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    public SItemStackLight setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public SItemStackLight setDisplayname(String name) {
        this.displayName = name;
        return this;
    }

    public SItemStackLight addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public SItemStackLight setItemLore(List<String> lores) {
        lore = lores;
        return this;
    }

    public SItemStackLight addEnchantment(Enchantment enchant, int level) {
        SEnchant s = new SEnchant(enchant, level);
        enchantments.add(s);
        return this;
    }

    public SItemStackLight addFlag(ItemFlag itemFlag) {
        this.flags.add(itemFlag);
        return this;
    }

    public SItemStackLight setFlags(List<ItemFlag> itemFlags) {
        this.flags = flags;
        return this;
    }

    public SItemStackLight setDamage(int damage) {
        this.damage = damage;
        ItemStack item = new ItemStack(Material.AIR, 1, (short) 1);
        return this;
    }

    public SItemStackLight setGlowingEffect(boolean enabled) {
        isGlowing = enabled;
        return this;
    }

    public SItemStackLight setUnBreakable(boolean enabled) {
        isUnbreakable = enabled;
        return this;
    }

    public SItemStackLight setSkullPlayer(String skullOwner) {
        this.skullMode = true;
        this.item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        this.skullOwner = skullOwner;
        return this;
    }

    public SItemStackLight setSkullUrl(String url) {
        this.skullMode = true;
        this.item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        this.url = url;
        return this;
    }

    private void resetSettings() {
        item = null;
        amount = 0;
        item = null;
        amount = 1;
        displayName = null;
        lore = new ArrayList<>();
        enchantments = new ArrayList<>();
        flags = new ArrayList<>();
        damage = 0;
        isGlowing = false;
        isUnbreakable = false;
        skullMode = false;
        url = null;
        skullOwner = null;
    }

    public SItemStackLight convertItemStackToSItemStackLight(ItemStack item) {
        resetSettings();
        this.item = new ItemStack(item.getType());
        this.amount = item.getAmount();
        this.displayName = item.getItemMeta().getDisplayName();
        this.lore = item.getItemMeta().getLore();
        for (int i = 0; i < item.getItemMeta().getEnchants().size(); i++) {
            SEnchant se = new SEnchant((Enchantment) item.getEnchantments().keySet().toArray()[i], item.getEnchantments().get(item.getEnchantments().keySet().toArray()[i]));
            this.enchantments.add(se);
        }
        this.damage = item.getDurability();
        this.isGlowing = item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS);
        this.isUnbreakable = item.getItemMeta().isUnbreakable();

        return this;
    }

    public ItemStack build() {
        ItemStack item = this.item;
        item.setAmount(this.amount);
        item.setDurability((short) this.damage);
        if (!skullMode) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(this.displayName);
            if (this.lore == null || this.lore.size() == 0) {
                itemMeta.setLore(this.lore);
            }
            itemMeta.setUnbreakable(this.isUnbreakable);
            for (int i = 0; i < this.enchantments.size(); i++) {
                itemMeta.addEnchant(this.enchantments.get(i).ench, this.enchantments.get(i).level, true);
            }
            for (int i = 0; i < this.flags.size(); i++) {
                itemMeta.addItemFlags(this.flags.get(i));
            }
            if (this.isGlowing) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addEnchant(Enchantment.LURE, 1, true);
            }
            item.setItemMeta(itemMeta);
        } else {
            item.setDurability((short) 3);
            SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
            if (skullOwner != null) {
                itemMeta.setOwner(this.skullOwner);
            }
            if (url != null) {
                loadProfile(itemMeta, url);
            }
            if (!this.lore.isEmpty()) {
                itemMeta.setLore(this.lore);
            }
            itemMeta.setUnbreakable(this.isUnbreakable);
            for (int i = 0; i < this.enchantments.size(); i++) {
                itemMeta.addEnchant(this.enchantments.get(i).ench, this.enchantments.get(i).level, true);
            }
            for (int i = 0; i < this.flags.size(); i++) {
                itemMeta.addItemFlags(this.flags.get(i));
            }
            if (this.isGlowing) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addEnchant(Enchantment.LURE, 1, true);
            }
            item.setItemMeta(itemMeta);
        }
        return item;
    }


    private void loadProfile(ItemMeta meta, String url) {

        Class<?> profileClass = Reflection.getClass("com.mojang.authlib.GameProfile");

        Constructor<?> profileConstructor = Reflection.getDeclaredConstructor(profileClass, UUID.class, String.class);

        Object profile = Reflection.newInstance(profileConstructor, UUID.randomUUID(), null);

        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        Method getPropertiesMethod = Reflection.getDeclaredMethod(profileClass, "getProperties");

        Object propertyMap = Reflection.invoke(getPropertiesMethod, profile);

        Class<?> propertyClass = Reflection.getClass("com.mojang.authlib.properties.Property");

        Reflection.invoke(
                Reflection.getDeclaredMethod(
                        ForwardingMultimap.class, "put", Object.class, Object.class
                ),
                propertyMap,
                "textures",
                Reflection.newInstance(Reflection.getDeclaredConstructor(propertyClass, String.class, String.class), "textures", new String(encodedData))
        );

        Reflection.setField("profile", meta, profile);
    }

    private static final class Reflection {

        private static Class<?> getClass(String forName) {
            try {
                return Class.forName(forName);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... params) {
            try {
                return clazz.getDeclaredConstructor(params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static <T> T newInstance(Constructor<T> constructor, Object... params) {
            try {
                return constructor.newInstance(params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                return null;
            }
        }

        private static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
            try {
                return clazz.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static Object invoke(Method method, Object object, Object... params) {
            method.setAccessible(true);
            try {
                return method.invoke(object, params);
            } catch (InvocationTargetException | IllegalAccessException e) {
                return null;
            }
        }

        private static void setField(String name, Object instance, Object value) {
            Field field = getDeclaredField(instance.getClass(), name);
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException ignored) {
            }
        }

        private static Field getDeclaredField(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return null;
            }
        }
    }
}



class SInventoryLight{

    Inventory inv = null;
    String title;

    public SInventoryLight(int rows, String title) {
        inv = Bukkit.createInventory(null, rows * 9, title);
        this.title = title;
    }

    public SInventoryLight setItem(int slot, ItemStack item) {
        inv.setItem(slot, item);
        return this;
    }

    public SInventoryLight setItem(int[] slots, ItemStack item) {
        for (int i = 0; i < slots.length; i++) {
            inv.setItem(slots[i], item);
        }
        return this;
    }

    public SInventoryLight fillInventory(ItemStack item) {
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, item);
        }
        return this;
    }

    public SInventoryLight setTitle(String title) {
        this.title = title;
        return this;
    }

    public SInventoryLight clear() {
        this.inv.clear();
        return this;
    }

    public Inventory build() {
        return inv;
    }
}


class GachaBannerDictionaryLight {
    HashMap<Integer, ItemStack> banner = new HashMap<>();

    public GachaBannerDictionaryLight(){
        banner.put(0, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n0").build());
        banner.put(1, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n1").build());
        banner.put(2, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n2").build());
        banner.put(3, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n3").build());
        banner.put(4,new SBannerItemStackLight((short)15).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n4").build());
        banner.put(5,new SBannerItemStackLight((short)15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER)).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n5").build());
        banner.put(6, new SBannerItemStackLight((short) 0).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n6").build());
        banner.put(7, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n7").build());
        banner.put(8, new SBannerItemStackLight((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n8").build());
        banner.put(9, new SBannerItemStackLight((short) 0).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).setDisplayName("§a§l§n9").build());
    }

    public ItemStack getItem(int id){
        return banner.get(id);
    }
}

class SBannerItemStackLight {
    ItemStack banner = new ItemStack(Material.BANNER);

    public SBannerItemStackLight(short color){
        banner.setDurability(color);
    }

    public SBannerItemStackLight pattern(Pattern pattern) {
        BannerMeta meta = (BannerMeta)banner.getItemMeta();
        meta.addPattern(pattern);
        banner.setItemMeta(meta);
        return this;
    }

    public SBannerItemStackLight patterns(List<Pattern> patterns){
        BannerMeta meta = (BannerMeta)banner.getItemMeta();
        for(Pattern pat : patterns){
            meta.addPattern(pat);
        }
        banner.setItemMeta(meta);
        return this;
    }
    
    public SBannerItemStackLight setDisplayName(String name){
        ItemMeta item = banner.getItemMeta();
        item.setDisplayName(name);
        banner.setItemMeta(item);
        return this;
    }

    public ItemStack build(){
        ItemMeta item = banner.getItemMeta();
        item.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.banner.setItemMeta(item);
        return this.banner;
    }


}

