package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class Arena {

    private final ParkourPlugin plugin;

    public Arena(ParkourPlugin parkourPlugin) {
        this.plugin = parkourPlugin;
    }

    public Set<String> getArenas() {
        return plugin.getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false);
    }
    public boolean onArena(Player player) {
        try {
            World world = player.getLocation().getWorld();
            for(String worlds : plugin.getStringWorlds()) {
                if(world.getName().equalsIgnoreCase(worlds)) {
                    return true;
                }
            }
        } catch(NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(plugin.PREFIX + "Error catched! No arenas, ignoring...");
        }

        return false;
    }
    public String getWorldName(Player player) {
        return player.getLocation().getWorld().getName();
    }
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public ParkourPlugin getMainClass() {
        return plugin;
    }
    public String getWorldUUID(World name) {
        return name.getUID().toString();
    }
}
