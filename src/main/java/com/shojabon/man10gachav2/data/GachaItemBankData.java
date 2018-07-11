package com.shojabon.man10gachav2.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sho on 2018/07/10.
 */
public class GachaItemBankData {

    int id;
    long amount;

    public GachaItemBankData(int id, long amount){
        this.id = id;
        this.amount = amount;
    }

    public Map<String, String> getStringData(){
        Map<String, String> out = new HashMap<>();
        out.put("itemType", String.valueOf(id));
        out.put("itemAmount", String.valueOf(amount));
        return out;
    }

}
