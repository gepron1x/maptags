package me.gepron1x.maptags.utlis;

import java.util.ArrayList;


import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.utility.Util;

import me.gepron1x.maptags.MapTagsPlugin;

public class Colors {

	public static String paint(String s) {
		return ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', s);
	}

	public static List<String> stringAsList(String lore) {
		lore = paint(lore.replace('_', ' '));
		List<String> lorear = new ArrayList<String>();
		if (lore.split(";").length != 1) {
			lorear = Arrays.asList(lore.split(";"));
		} else {
			lorear = Util.asList(lore);
		}
		return paintList(lorear);
	}

	public static String buildName(String s) {
		s = paint(ChatColor.WHITE + s.replace('_', ' '));
		return s;
	}

	public static ItemStack buildIcon(ItemStack e) {
		if (e == null || e.getType() == Material.AIR)
			e = new ItemStack(Material.BEDROCK);
		return e;
	}

	public static List<String> paintList(List<String> s) {
		List<String> result = new ArrayList<String>();
		for (String stroke : s) {
			result.add(ChatColor.GRAY + paint(stroke));
		}
		return result;

	}
	public static ItemStack buildItemStackFromConfig(String path) {
		MapTagsPlugin main = MapTagsPlugin.getInstance();
		ItemStack e = new ItemStack(Material.getMaterial(main.getConfig().getString(path + ".material")),
				main.getConfig().getInt(path + ".amount"));
		ItemMeta meta = e.getItemMeta();
		meta.setDisplayName(Colors.paint(main.getConfig().getString(path + ".name")));
		meta.setLore(Colors.paintList(main.getConfig().getStringList(path + ".lore")));
		e.setItemMeta(meta);
		return e;
		
	}

}
