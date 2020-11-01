package com.github.thethingyee.parkourplugin.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.scoreboards.ArenaScoreboard;
import com.github.thethingyee.parkourplugin.scoreboards.HubScoreboard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class ParkourJoinListeners implements Listener {

    private final ParkourPlugin parkourPlugin;
    private final HubScoreboard hubScoreboard;
    private final ArenaScoreboard arenaScoreboard;

    public ParkourJoinListeners(ParkourPlugin parkourPlugin, HubScoreboard hubScoreboard, ArenaScoreboard arenaScoreboard) {
        this.parkourPlugin = parkourPlugin;
        this.hubScoreboard = hubScoreboard;
        this.arenaScoreboard = arenaScoreboard;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String maintitle = ChatColor.translateAlternateColorCodes('&', parkourPlugin.getConfig().getString("messages.onJoin.jointitle.maintitle"));
        String subtitle = ChatColor.translateAlternateColorCodes('&', parkourPlugin.getConfig().getString("messages.onJoin.jointitle.subtitle"));
        int fadein = parkourPlugin.getConfig().getInt("messages.onJoin.jointitle.fadeintime");
        int showtime = parkourPlugin.getConfig().getInt("messages.onJoin.jointitle.showouttime");
        int fadeout = parkourPlugin.getConfig().getInt("messages.onJoin.jointitle.fadeouttime");

        try {
            Player player = event.getPlayer();
            double x = parkourPlugin.getConfig().getDouble("hubspawn.X");
            double y = parkourPlugin.getConfig().getDouble("hubspawn.Y");
            double z = parkourPlugin.getConfig().getDouble("hubspawn.Z");
            World world = Bukkit.getWorld(UUID.fromString(parkourPlugin.getConfig().getString("hubspawn.World")));

            Location location = new Location(world, x, y, z);
            player.teleport(location);
            for(Player target : Bukkit.getOnlinePlayers()) {
                if(target.getLocation().getWorld().getUID().equals(UUID.fromString(parkourPlugin.getConfig().getString("hubspawn.World")))) {
                    hubScoreboard.access(target);
                }
                try {
                    for(String arenaWorlds : parkourPlugin.getArenaConfig().getConfigurationSection("parkourarenas.worlds").getKeys(false)) {
                        if(target.getLocation().getWorld().equals(Bukkit.getWorld(arenaWorlds))) {
                            arenaScoreboard.access(target);
                        }
                    }
                } catch(NullPointerException e) {
                    Bukkit.getConsoleSender().sendMessage(parkourPlugin.PREFIX + ChatColor.RED + "Catched error! No arenas found.");
                }
            }


            parkourPlugin.getTitle().send(player, maintitle, subtitle, fadein, showtime, fadeout);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
            String joinmsg = ChatColor.translateAlternateColorCodes('&', parkourPlugin.getConfig().getString("messages.onJoin.joinmsg").replace("{player}", player.getDisplayName()));
            player.sendMessage(joinmsg);

        } catch(IllegalArgumentException e) {
            // catches the spawn: null error
            Bukkit.getConsoleSender().sendMessage(parkourPlugin.PREFIX + ChatColor.RED + "Error catched. Spawn not set, set it by /parkour setspawn");
            e.printStackTrace();
        }
    }
}
