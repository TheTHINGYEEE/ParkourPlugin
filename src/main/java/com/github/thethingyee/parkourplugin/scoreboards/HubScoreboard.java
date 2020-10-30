package com.github.thethingyee.parkourplugin.scoreboards;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class HubScoreboard {

    public final ParkourPlugin parkourPlugin;

    public HubScoreboard(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    public void access(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("hub", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour");

        Score score13 = objective.getScore("");
        Score score12 = objective.getScore("You are currently");
        Score score11 = objective.getScore("on: " + ChatColor.GREEN + "ParkourSpawn");
        Score score10 = objective.getScore(" ");
        Score score9 = objective.getScore(ChatColor.WHITE + "Compete against");
        Score score8 = objective.getScore(ChatColor.WHITE + "others!");
        Score score7 = objective.getScore("  ");
        Score score6 = objective.getScore(ChatColor.RED + "Plugin still");
        Score score5 = objective.getScore(ChatColor.RED + "on maintenance!");
        Score score4 = objective.getScore("   ");
        Score score3 = objective.getScore("     ");
        Score score2 = objective.getScore(ChatColor.WHITE + "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
        Score score1 = objective.getScore("      ");
        Score score0 = objective.getScore(ChatColor.translateAlternateColorCodes('&', parkourPlugin.getConfig().getString("messages.hubscoreboard.server-ip")));

        score0.setScore(0);
        score1.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        score4.setScore(4);
        score5.setScore(5);
        score6.setScore(6);
        score7.setScore(7);
        score8.setScore(8);
        score9.setScore(9);
        score10.setScore(10);
        score11.setScore(11);
        score12.setScore(12);
        score13.setScore(13);

        player.setScoreboard(scoreboard);
    }
}
