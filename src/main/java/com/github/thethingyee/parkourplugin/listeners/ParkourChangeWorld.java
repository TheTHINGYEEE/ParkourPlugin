package com.github.thethingyee.parkourplugin.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.scoreboards.ArenaScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.HubScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.ParkourScoreboards;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

public class ParkourChangeWorld extends ParkourScoreboards implements Listener {

    public ParkourChangeWorld(HubScoreboard hubScoreboard, ArenaScoreboard arenaScoreboard, ParkourPlugin parkourPlugin) {
        super(hubScoreboard, arenaScoreboard, parkourPlugin);
    }

    @EventHandler
    public void onChangeWorlds(PlayerChangedWorldEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            for(String arenas : this.getParkourPlugin().getStringWorlds()) {
                World to = Bukkit.getWorld(arenas);
                if(player.getWorld().equals(to)) {
                    this.getArenaScoreboard().access(player);
                }
            }
            World hub = Bukkit.getWorld(UUID.fromString(this.getParkourPlugin().getConfig().getString("hubspawn.World")));
            if(player.getWorld().equals(hub)) {
                this.getHubScoreboard().access(player);
            }
        }
    }
}
