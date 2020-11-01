package com.github.thethingyee.parkourplugin.SubCore.managers;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaManager extends Arena {

    public ArenaManager(ParkourPlugin parkourPlugin) {
        super(parkourPlugin);
    }

    public void joinArena(Player player, String worlduuid) {

        UUID uuid = UUID.fromString(worlduuid);
        World world = Bukkit.getWorld(uuid);

        if (world != null) {
            if (this.getArenas().contains(world.getName())) {
                player.sendMessage(this.getMainClass().PREFIX + ChatColor.GRAY + "Sending you to: " + ChatColor.GOLD + "" + ChatColor.BOLD + world.getUID().toString());
                player.teleport(world.getSpawnLocation());
            } else {
                player.sendMessage(this.getMainClass().PREFIX + ChatColor.RED + "That world isn't added to the database.");
            }
        } else {
            player.sendMessage(this.getMainClass().PREFIX + ChatColor.RED + "That world doesn't exist.");
        }
    }
    public boolean arenaInDatabase(String name) {
        if(this.getArenas().contains(name) && Bukkit.getWorld(name) != null) {
            return true;
        }
        return false;
    }
}
