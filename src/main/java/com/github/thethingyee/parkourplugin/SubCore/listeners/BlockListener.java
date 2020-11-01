package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends SubCore implements Listener {
    public BlockListener(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }

    // To bypass, use parkourplugin.bypass.blockplacement on the perms of the player

    @EventHandler(priority = EventPriority.HIGH)
    public void blockPlace(BlockPlaceEvent event) {
        // if that player who placed that block is the owner of that arena. it will return it.
        if(this.getArena().onArena(event.getPlayer())) {
            if(this.getPlayerManager()
                    .getUUIDofArenaOwner(
                            event.getPlayer()
                                    .getLocation()
                                    .getWorld()
                                    .getName()
                    ).equals(event.getPlayer().getUniqueId().toString()) ||
                    event.getPlayer().isOp() ||
                    event.getPlayer().hasPermission("parkourplugin.bypass.blockplacement") ||
                    this.getPlayerManager().onHub(event.getPlayer())) return;

            event.setCancelled(true);
            event.getPlayer().sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but you're not allowed to build here.");
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void blockBreak(BlockBreakEvent event) {

        if(this.getArena().onArena(event.getPlayer())) {
            if(this.getPlayerManager()
                    .getUUIDofArenaOwner(
                            event.getPlayer()
                                    .getLocation()
                                    .getWorld()
                                    .getName()
                    ).equals(
                            event.getPlayer()
                                    .getUniqueId()
                                    .toString()
                    ) || event.getPlayer().isOp() ||
                    event.getPlayer().hasPermission("parkourplugin.bypass.blockplacement") ||
                    this.getPlayerManager().onHub(event.getPlayer())) return;

            event.setCancelled(true);
            event.getPlayer().sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but you're not allowed to break blocks here.");
        }
    }
}
