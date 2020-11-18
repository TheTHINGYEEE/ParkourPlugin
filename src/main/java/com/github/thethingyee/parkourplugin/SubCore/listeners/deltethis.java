package com.github.thethingyee.parkourplugin.SubCore.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class deltethis implements Listener {

    @EventHandler
    public void breakEvent(BlockBreakEvent e) {
        Location blockloc = new Location(e.getPlayer().getWorld(), 0, 47, 0);
        if(e.getBlock().getLocation().equals(blockloc)) {
            if(!(e.getBlock().getType() == Material.LEAVES)) {
                for(ItemStack is : e.getBlock().getDrops()) {
                    e.getPlayer().getWorld().dropItemNaturally(blockloc, is);
                }
                e.setCancelled(true);
                e.getBlock().setType(Material.LEAVES);
            }
        }
    }
}
