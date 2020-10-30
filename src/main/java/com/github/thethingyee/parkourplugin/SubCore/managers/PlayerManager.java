package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlayerManager extends ArenaManager {

    public PlayerManager(ParkourPlugin parkourPlugin) {
        super(parkourPlugin);
    }

    public String getUUIDofArenaOwner(String name) {
        return this.getMainClass().getConfig().getString("parkourarenas.worlds." + name + ".owner");
    }
    public List<Player> getPlayersOnArena(String worlduuid) {
        World world = Bukkit.getWorld(UUID.fromString(worlduuid));
        return world.getPlayers();
    }
    public int getNumberOnlinePlayersOnArena(String worlduuid) {
        World world = Bukkit.getWorld(UUID.fromString(worlduuid));
        return world.getPlayers().size();
    }
    public int getNumberGlobalOnlinePlayers() {
        return Bukkit.getOnlinePlayers().size();
    }
    public Collection<? extends Player> getGlobalOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }
}
