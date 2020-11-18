package com.github.thethingyee.parkourplugin.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class ParkourMobSpawn implements Listener {

    public final ParkourPlugin parkourPlugin;

    public ParkourMobSpawn(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof ArmorStand) return;
        int count = 0;
        try {
            for (String things : parkourPlugin.getStringWorlds()) {
                World world = Bukkit.getWorld(things);
                if (world != null) {
                    event.setCancelled(true);
                }
            }
        } catch(NullPointerException e) {
            count++;
            if(count < 1) {
                Bukkit.getConsoleSender().sendMessage(parkourPlugin.PREFIX + "No worlds set on config.yml");
            }
        }
    }
}
