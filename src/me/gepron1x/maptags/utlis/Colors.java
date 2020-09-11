package me.gepron1x.maptags.utlis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Colors {
	
	public static String paint(String s) {
		return ChatColor.RESET + ChatColor.translateAlternateColorCodes('&',s);
	}

	public static List<String> stringAsList(String lore) {
		lore = paint(lore.replace('_', ' '));
		List<String> lorear = new ArrayList<String>();
		if (lore.split(";").length != 1) {
			lorear = Arrays.asList(lore.split(";"));
		} else {
			List<String> to = new ArrayList<String>();
			lorear = to;
		}
		return lorear;
	}
	public static String buildName(String s) {
		s = paint(s.replace('_', ' '));
		return s;
	}
	public static ItemStack buildIcon(ItemStack e) {
		if (e == null || e.getType() == Material.AIR)
			e = new ItemStack(Material.BEDROCK);
		return e;
	}
	public static List<String> paintList(List<String> s) {
		List<String> result = new ArrayList<String>();
		for(String stroke : s) {
			result.add(paint(stroke));
		}
		return result;
		
	}
  public static ItemStack buildItemStackFromConfig(FileConfiguration cfg,String path) {
	  ItemStack e = new ItemStack(Material.getMaterial(cfg.getString(path+".material")),cfg.getInt(path+".amount"));
	  ItemMeta meta = e.getItemMeta();
	  meta.setDisplayName(paint(cfg.getString(path+".name")));
	  meta.setLore(paintList(cfg.getStringList(path+".lore")));
	  e.setItemMeta(meta);
	return e;
	  
  }
	

}
