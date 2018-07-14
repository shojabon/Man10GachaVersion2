package com.shojabon.man10gachav2.data.GachaPaymentData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sho on 2018/07/14.
 */
public class GachaItemBankPayment {
    private int id;
    private long amount;

    public GachaItemBankPayment(int id, long amount){
        this.id = id;
        this.amount = amount;
    }

    public int getId(){
        return this.id;
    }

    public long getAmount(){
        return this.amount;
    }

    public Map<String, String> getStringData(){
        Map<String, String> out = new HashMap<>();
        out.put("itemType", String.valueOf(id));
        out.put("itemAmount", String.valueOf(amount));
        return out;
    }
}
