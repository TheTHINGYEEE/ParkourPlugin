package com.github.thethingyee.parkourplugin.SubCore.listeners;

import com.github.thethingyee.parkourplugin.ParkourPlugin;
import com.github.thethingyee.parkourplugin.SubCore.GUI.ParkourGUI;
import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import com.github.thethingyee.parkourplugin.SubCore.managers.Arena;
import com.github.thethingyee.parkourplugin.SubCore.managers.ArenaManager;
import com.github.thethingyee.parkourplugin.SubCore.managers.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;

public class InteractNCWarps extends SubCore implements Listener {

    public HashMap<Player, Boolean> enterWarpName = new HashMap<>();

    public final ParkourGUI parkourGUI;

    public InteractNCWarps(Arena arena, ArenaManager arenaManager, PlayerManager playerManager, ParkourGUI parkourGUI, ParkourPlugin parkourPlugin) {
        super(arena, arenaManager, playerManager, parkourPlugin);
        this.parkourGUI = parkourGUI;
    }

    @EventHandler
    public void interactEvent(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) e.getRightClicked();
            if(armorStand.getName().equals(ChatColor.RED + "Warp not connected.")) {
                if(this.getPlayerManager().onArena(e.getPlayer())) {
                    if(this.getPlayerManager().getUUIDofArenaOwner(e.getPlayer().getWorld().getName()).equals(e.getPlayer().getUniqueId().toString())) {
                        parkourGUI.loadWarpGraphicalUserInterface(e.getPlayer(), armorStand.getUniqueId().toString());
                    } else {
                        e.getPlayer().sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but the owner could only do this.");
                    }
                } else {
                    e.getPlayer().sendMessage(this.getParkourPlugin().PREFIX + ChatColor.RED + "Sorry, but you need to be in a arena to do this.");
                }
            }
        }
    }
    @EventHandler
    public void invInteractEvent(InventoryClickEvent event) {
        if(event.getClickedInventory().getName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour Arenas")) {
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Yes")) {
                enterWarpName.put(((Player) event.getWhoClicked()), true);
            } else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "No")) {
                ((Player)event.getWhoClicked()).sendMessage(this.getParkourPlugin().PREFIX + "");
            }
        }
    }
    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        if(enterWarpName.containsKey(event.getPlayer())) {
            if(enterWarpName.get(event.getPlayer())) {
                String name = event.getMessage();
                if(this.getParkourPlugin().getWarpManager().getWarps().contains(name)) {

                }
            }
        }
    }
}
