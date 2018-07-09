package com.shojabon.man10gachav2.data;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by sho on 2018/07/09.
 */
class GachaTeleport {
    Location location;
    public GachaTeleport(Location location){
        this.location = location;
    }

    public void teleportPlayerToLocation(Player player){
        player.teleport(location);
    }


}
