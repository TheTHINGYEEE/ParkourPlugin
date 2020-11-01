package com.github.thethingyee.parkourplugin.SubCore.GUI;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class GUIClickEvent extends SubCore implements Listener {

    public GUIClickEvent(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory().getName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour Arenas")) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.GRASS) {
                String worlduuid = e.getCurrentItem().getItemMeta().getLore().get(1);
                World world = Bukkit.getWorld(UUID.fromString(worlduuid));
                if(world != null) {
                    this.getPlayerManager().joinArena(player, world.getUID().toString());
                } else {
                    player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "That world doesn't exist!");
                }
            }
        }
    }
}
