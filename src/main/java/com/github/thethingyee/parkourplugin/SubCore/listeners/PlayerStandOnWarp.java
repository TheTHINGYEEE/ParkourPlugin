package com.github.thethingyee.parkourplugin.SubCore.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerStandOnWarp implements Listener {

    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        double cordsunderneath = player.getLocation().getY() - 1;
        Block block = player.getWorld().getBlockAt(new Location(world, player.getLocation().getX(), cordsunderneath, player.getLocation().getZ()));

        if(block.getType() == Material.ENDER_PORTAL_FRAME) {

        }
    }
}
