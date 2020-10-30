package com.github.thethingyee.parkourplugin.scoreboards;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ArenaScoreboard {

    public final ParkourPlugin parkourPlugin;

    public ArenaScoreboard(ParkourPlugin parkourPlugin) {
        this.parkourPlugin = parkourPlugin;
    }

    public void access(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("arena", "dummy");
        objective.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score score13 = objective.getScore("");
        Score score12 = objective.getScore(ChatColor.WHITE + "Arena: " + ChatColor.GREEN + player.getWorld().getName());
        Score score11 = objective.getScore(" ");
        Score score10 = objective.getScore(ChatColor.WHITE + "Compete against");
        Score score9 = objective.getScore(ChatColor.WHITE + "others!");
        Score score8 = objective.getScore("  ");
        Score score7 = objective.getScore(ChatColor.RED + "Plugin still");
        Score score6 = objective.getScore(ChatColor.RED + "on maintenance!");
        Score score5 = objective.getScore("   ");
        Score score4 = objective.getScore(ChatColor.WHITE + "Arena Players: " + ChatColor.GREEN + player.getWorld().getPlayers().size());
        Score score3 = objective.getScore(ChatColor.WHITE + "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
        Score score2 = objective.getScore("    ");
        Score score1 = objective.getScore("     ");
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
