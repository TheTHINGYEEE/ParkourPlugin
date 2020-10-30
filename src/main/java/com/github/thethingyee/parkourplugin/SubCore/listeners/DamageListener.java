package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener extends SubCore implements Listener {

    public DamageListener(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }

    @EventHandler
    public void damgeEvent(EntityDamageByEntityEvent event) {
        if(this.getPlayerManager()
                .getUUIDofArenaOwner(
                        ((Player) event.getDamager())
                                .getLocation()
                                .getWorld()
                                .getName())
                .equals(
                        ((Player) event.getDamager())
                                .getUniqueId()
                                .toString()
                )) return;
        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but you're not allowed to hit a player.");
        }
    }
}
