package com.shojabon.man10gachav2.data;

import com.shojabon.man10gachav2.apis.SItemStack;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemBankPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaItemStackPayment;
import com.shojabon.man10gachav2.data.GachaPaymentData.GachaVaultPayment;
import com.shojabon.man10gachav2.enums.GachaPaymentType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sho on 2018/07/14.
 */
public class GachaPayment {
    private GachaPaymentType type;
    private GachaItemStackPayment itemStackPayment;
    private GachaItemBankPayment itemBankPayment;
    private GachaVaultPayment vaultPayment;

    public GachaPayment(GachaItemStackPayment itemPayment){
        this.type = GachaPaymentType.ITEM;
        this.itemStackPayment = itemPayment;
    }

    public GachaPayment(GachaItemBankPayment itemBankPayment){
        this.type = GachaPaymentType.ITEM_BANK;
        this.itemBankPayment = itemBankPayment;
    }
    public GachaPayment(GachaVaultPayment vaultPayment){
        this.type = GachaPaymentType.VAULT;
        this.vaultPayment = vaultPayment;
    }

    public Map<String, String> getStringData(){
        Map<String, String> out = new HashMap<>();
        out.put("type", type.toString());
        if(type == GachaPaymentType.VAULT){
            out.put("amount", String.valueOf(vaultPayment.getValue()));
        }
        if(type == GachaPaymentType.ITEM){
            out.put("item", new SItemStack(itemStackPayment.getItemStack()).toBase64());
            out.put("amount", String.valueOf(itemStackPayment.getAmount()));
        }
        if(type == GachaPaymentType.ITEM_BANK){
            out.put("item", String.valueOf(itemBankPayment.getId()));
            out.put("amount", String.valueOf(itemBankPayment.getAmount()));
        }
        return out;
    }


}
