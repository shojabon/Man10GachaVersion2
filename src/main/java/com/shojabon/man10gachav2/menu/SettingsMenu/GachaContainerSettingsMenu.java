package com.shojabon.man10gachav2.menu.SettingsMenu;

import com.shojabon.man10gachav2.GachaGame;
import com.shojabon.man10gachav2.Man10GachaAPI;
import com.shojabon.man10gachav2.apis.SInventory;
import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.GachaBannerDictionary;
import com.shojabon.man10gachav2.data.GachaFinalItemStack;
import com.shojabon.man10gachav2.data.GachaItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class GachaContainerSettingsMenu {
    Inventory inv;
    Listener listener = new Listener();
    JavaPlugin plugin;
    String gacha;
    Player p;
    String title;
    Function<InventoryClickEvent, String> cancelFunction;
    GachaBannerDictionary bannerDictionary = new GachaBannerDictionary();
    GachaGame game;

    HashMap<Integer, GachaFinalItemStack> items = new HashMap<>();
    HashMap<String, GachaItemStack> itemStackMap = new HashMap<>();



    //page parameters
    int totalPages = 0;
    int currentPage = 0;

    private void calculations(){
        for(int i = 0;i < game.getStorage().size();i++){
            items.put(i, game.getStorage().get(i));
        }
        totalPages = items.size()/54;
        if(items.size()/54 - totalPages > 0) totalPages += 1;
        if(totalPages == 0) totalPages = 1;
    }

    private String encodeString(String string){
        char[] cha = string.toCharArray();
        StringBuilder res = new StringBuilder("§m§a§n§1§0");
        for (char aCha : cha) {
            res.append("§").append(aCha);
        }
        return res.toString();
    }

    private String decodeString(String string){
        string = string.replace("§m§a§n§1§0", "");
        return string.replaceAll("§", "");
    }

    public GachaContainerSettingsMenu(Player p, String title, String gacha, Function<InventoryClickEvent, String> cancelFunction){
        p.closeInventory();
        this.cancelFunction = cancelFunction;
        this.gacha = gacha;
        this.p = p;
        game = Man10GachaAPI.gachaGameMap.get(gacha);
        this.title = title;
        this.plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Man10GachaV2");
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        SInventory i = new SInventory(9, title);
        i.setItem(new int[]{72,79, 54,55,56,57,58,59,60,61,62,70,71,73,63,64}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(11).setDisplayname(" ").build());
        i.setItem(new int[]{54,55}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l§n次へ").build());
        i.setItem(new int[]{62,61}, new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l§n前へ").build());


        i.setItem(65, new SItemStack(Material.BARRIER).setDisplayname("§c§l選択中のアイテムを解除する").build());
        i.setItem(66, new SItemStack(Material.SPECTRAL_ARROW).setDisplayname("§d§lスポイトツール").build());
        i.setItem(68, new SItemStack(Material.ARROW).setDisplayname("§f§lペンツール").build());
        i.setItem(69, new SItemStack(Material.BUCKET).setDisplayname("§b§lバケツツール").build());

        i.setItem(58, new SItemStack(Material.MINECART).setDisplayname("§7§lページジャンプツール").build());
        i.setItem(74, new SItemStack(Material.TNT).setDisplayname("§c§lページ削除ツール").build());
        i.setItem(75, new SItemStack(Material.BOOK_AND_QUILL).setDisplayname("§8§lページコピーツール").build());
        i.setItem(76, new SItemStack(Material.BOOK).setDisplayname("§6§lページインフォツール").build());
        i.setItem(77, new SItemStack(Material.PAINTING).setDisplayname("§e§l同一アイテムページ作成ツール").build());
        i.setItem(78, new SItemStack(Material.PAPER).setDisplayname("§f§l新規ページ作成ツール").build());

        i.setItem(80, bannerDictionary.getSymbol("back"));

        for(int ii = 0;ii < game.getItemIndex().size();ii++){
            itemStackMap.put(game.getItemIndex().get(ii).getComparisonString(), game.getItemIndex().get(ii));
        }

        inv = i.build();
        calculations();
        renderItems();
        renderMenu();
        p.openInventory(inv);
    }

    private void renderMenu(){
        renderPageSelectionButton();
    }

    private void renderPageSelectionButton(){
        SItemStack nextButton =  new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l§n次へ").addLore("§c§l現在のページ:" + (currentPage + 1) + "/" + (totalPages + 1));
        SItemStack previousButton =  new SItemStack(Material.STAINED_GLASS_PANE).setDamage(14).setDisplayname("§c§l§n前へ").addLore("§c§l現在のページ:" + (currentPage + 1) + "/" + (totalPages + 1));
        inv.setItem(61, nextButton.build());
        inv.setItem(62, nextButton.build());

        inv.setItem(54, previousButton.build());
        inv.setItem(55, previousButton.build());
    }

    private void renderItems(){
        int size = items.size();
        if(size - currentPage * 54>= 54) {
            size = 54;
        }else{
            size = size - currentPage * 54;
        }
        for(int i = 0;i < 54;i++){
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        for(int i = 0;i < size;i++){
            if(items.get(currentPage * 54 + i).doesExist()){
                GachaFinalItemStack gItemStack = items.get(currentPage * 54 + i);
                ItemStack item = gItemStack.getItemStack().item.clone();
                item.setAmount(gItemStack.getAmount());
                String str = gItemStack.getItemStack().getComparisonString();
                inv.setItem(i, new SItemStack(item).setLore(Collections.singletonList(str)).build());
            }else{
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    private void close(Player p){
        HandlerList.unregisterAll(listener);
        p.closeInventory();
    }

    private void pushPageToList(int page){
        for(int i = 0;i < 54;i++){
            if(inv.getItem(i) != null){
                items.put(page * 54 + i, new GachaFinalItemStack(new GachaItemStack(inv.getItem(i)), inv.getItem(i).getAmount()));
            }else{
                items.put(page * 54 + i, new GachaFinalItemStack(null, 0));
            }
        }
    }

    class Listener implements org.bukkit.event.Listener
    {

        @EventHandler
        public void onClick(InventoryClickEvent e){
            if(e.getWhoClicked().getUniqueId() != p.getUniqueId()) return;
            int r = e.getRawSlot();
            if(r >= 54 && r <= 80) e.setCancelled(true);
            if(r == 78){
                totalPages += 1;
                renderPageSelectionButton();
            }
            if(r == 61 || r == 62){
                if(currentPage + 1 > totalPages){
                    return;
                }
                pushPageToList(currentPage);
                currentPage += 1;
                renderItems();
                renderPageSelectionButton();
            }
            if(r == 54 || r == 55){
                if(currentPage - 1 < 0){
                    return;
                }
                pushPageToList(currentPage);
                currentPage -= 1;
                renderItems();
                renderPageSelectionButton();
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent e){
            if(e.getPlayer().getUniqueId() != p.getUniqueId()) return;
            close((Player) e.getPlayer());
        }

    }
}
