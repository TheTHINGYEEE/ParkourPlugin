package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener extends SubCore implements Listener {

    public DamageListener(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void damagebyEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            if(this.getPlayerManager().onArena(((Player) event.getDamager()))) {
                Player player = (Player) event.getDamager();
                player.sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but you're not allowed to hit a player.");
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void damagebyVoidEvent(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(this.getPlayerManager().onArena(player)) {
                if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setCancelled(true);
                    player.teleport(player.getLocation().getWorld().getSpawnLocation());
                    e.setDamage(0.0);
                } else if(e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
