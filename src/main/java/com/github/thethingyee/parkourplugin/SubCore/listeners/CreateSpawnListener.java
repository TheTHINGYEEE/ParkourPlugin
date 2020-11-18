package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreateSpawnListener extends SubCore implements Listener {

    public CreateSpawnListener(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }
    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getClickedBlock().getType() == Material.STONE_PLATE) {
            if(e.getClickedBlock().getLocation().subtract(0, -1, 0).getBlock().getType() == Material.REDSTONE_BLOCK) {
                player.setBedSpawnLocation(player.getLocation());
            }
        }
    }
}
