package com.github.thethingyee.parkourplugin.SubCore.GUI;

import com.github.thethingyee.parkourplugin.SubCore.SubCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ParkourGUI {

    private final SubCore subCore;

    public ParkourGUI(SubCore subCore) {
        this.subCore = subCore;
    }

    public void loadArenaGraphicalUserInterface(Player player) {
        ArrayList<String> arenas = new ArrayList<>(subCore.getParkourPlugin().getStringWorlds());

        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.GOLD + "" + ChatColor.BOLD + "Parkour Arenas");

        for(int i = 0; i < arenas.size(); i++) {
            ItemStack arenaitem = new ItemStack(Material.GRASS, 1);
            ItemMeta meta = arenaitem.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            ArrayList<String> lore = new ArrayList<>();
            String arenaOwner = subCore.getPlayerManager().getNameofArenaOwner(arenas.get(i));
            lore.add(ChatColor.GOLD + "Owner: " + ChatColor.GREEN + arenaOwner);
            lore.add(subCore.getArena().getWorldUUID(Bukkit.getWorld(arenas.get(i))));
            meta.setDisplayName(ChatColor.YELLOW + arenas.get(i));
            meta.setLore(lore);

            arenaitem.setItemMeta(meta);

            gui.addItem(arenaitem);
            player.openInventory(gui);
        }
    }
    public void loadWarpGraphicalUserInterface(Player player, String armorStandUUID) {

        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Connect");

        ItemStack yes = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
        ItemStack no = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        ItemStack worlduuid = new ItemStack(Material.GRASS, 1);

        ItemMeta yesmeta = yes.getItemMeta();
        yesmeta.setDisplayName(ChatColor.GREEN + "Yes");
        ArrayList<String> yeslore = new ArrayList<>();
        yeslore.add(ChatColor.DARK_GREEN + "To confirm, click on this.");
        yesmeta.setLore(yeslore);
        yesmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta nometa = no.getItemMeta();
        nometa.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        nometa.setDisplayName(ChatColor.RED + "No");
        ArrayList<String> nolore = new ArrayList<>();
        nolore.add(ChatColor.DARK_RED + "To decline, click on this.");
        nometa.setLore(nolore);

        ItemMeta uuidmeta = worlduuid.getItemMeta();
        uuidmeta.setDisplayName(ChatColor.AQUA + armorStandUUID);
        ArrayList<String> uuidlore = new ArrayList<>();
        uuidlore.add(ChatColor.DARK_AQUA + "UUID of the warp.");
        uuidlore.add(ChatColor.DARK_RED + "IMPORTANT!");
        uuidmeta.setLore(uuidlore);

        yes.setItemMeta(yesmeta);
        no.setItemMeta(nometa);
        worlduuid.setItemMeta(uuidmeta);

        gui.setItem(0, no);
        gui.setItem(4, worlduuid);
        gui.setItem(8, yes);

        player.openInventory(gui);
    }
}
