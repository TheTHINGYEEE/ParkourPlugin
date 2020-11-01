package com.github.thethingyee.parkourplugin.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.scoreboards.ArenaScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.HubScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.ParkourScoreboards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ParkourLeaveListeners extends ParkourScoreboards implements Listener {

    public ParkourLeaveListeners(HubScoreboard hubScoreboard, ArenaScoreboard arenaScoreboard, ParkourPlugin parkourPlugin) {
        super(hubScoreboard, arenaScoreboard, parkourPlugin);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        for(Player target : Bukkit.getOnlinePlayers()) {
            if(target.getLocation().getWorld().getUID().equals(UUID.fromString(this.getParkourPlugin().getConfig().getString("hubspawn.World")))) {

                new BukkitRunnable() {

                    int counter = 0;

                    @Override
                    public void run() {
                        counter++;
                        if(counter > 1) {
                            getHubScoreboard().access(target);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(getParkourPlugin(), 0, 20);
            }
            try {
                for(String arenaWorlds : this.getParkourPlugin().getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false)) {
                    if(target.getLocation().getWorld().equals(Bukkit.getWorld(arenaWorlds))) {
                        new BukkitRunnable() {

                            int counter = 0;

                            @Override
                            public void run() {
                                counter++;
                                if(counter > 1) {
                                    getArenaScoreboard().access(target);
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(getParkourPlugin(), 0, 20);
                    }
                }
            } catch(NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(getParkourPlugin().PREFIX + ChatColor.RED + "Catched error! No arenas found.");
            }
        }
    }
}