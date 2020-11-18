package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpListener implements Listener {

    private final SubCore subCore;

    public WarpListener(SubCore subCore) {
        this.subCore = subCore;
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Block blockunderneath = player.getLocation().subtract(0, 1, 0).getBlock();
        if(blockunderneath.getType() == Material.ENDER_PORTAL_FRAME) {
            List<Entity> nearbyEntities = player.getNearbyEntities(0, 2, 0);
            for(int i = 0; i < nearbyEntities.size(); i++) {
                if(nearbyEntities.get(i) instanceof ArmorStand) {
                    for(int d = 0; d < subCore.getParkourPlugin().getWarpManager().getWarps().size(); d++) {
                        List<String> warps = subCore.getParkourPlugin().getWarpManager().getWarps();
                        String configUUID = subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warps.get(d) + ".UUID");
                        String armorStandUUID = ((ArmorStand)nearbyEntities.get(i)).getUniqueId().toString();

                        if(configUUID.equals(armorStandUUID)) {
                            if(subCore.getParkourPlugin().getWarpManager().hasDestination(warps.get(d))) {
                                ArmorStand dest = (ArmorStand) Bukkit.getEntity(UUID.fromString(subCore.getParkourPlugin().getWarpsConfiguration().getString("warps.warpers." + warps.get(d) + ".Destination")));
                                Location destLocation = dest.getLocation().add(0, 1.35, 0);
                                player.teleport(destLocation);
                            }
                        }
                    }
                }
            }
        }
    }
}
