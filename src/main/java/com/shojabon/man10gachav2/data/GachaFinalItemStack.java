package com.shojabon.man10gachav2.data;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by sho on 2018/07/11.
 */
public class GachaFinalItemStack {
    GachaItemStack item;
    int amount;
    boolean exists = true;
    public GachaFinalItemStack(GachaItemStack itemStack, int amount){
        this.item = itemStack;
        this.amount = amount;
        if(item == null){
            exists = false;
        }
    }

    public boolean doesExist() {
        return exists;
    }

    public GachaItemStack getItemStack(){
        return this.item;
    }

    public int getAmount(){
        return this.amount;
    }

    public boolean isTheSame(GachaFinalItemStack itemStack){
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
            if (gzipOut != null) try { gzipOut.close(); } catch (IOException logOrIgnore) {}
        }
        return new String(byteaOut.toByteArray());
    }
}
