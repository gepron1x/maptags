package me.gepron1x.maptags.utlis;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.gson.Gson;

public class MapTag {

	private String id, name;
	private List<String> lore = new ArrayList<String>();
	private UUID owner;
	private Location location;
	private ItemStack icon;

	public MapTag(String id, String name, List<String> lore, UUID owner, Location loc, ItemStack icon) {
		this.id = id;
		this.name = name;
		this.lore = lore;
		this.icon = icon;
		this.owner = owner;
		this.location = loc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ItemStack toItemStack() {
		// ItemStack result = this.icon;
		List<String> loredump = new ArrayList<String>();
		loredump.addAll(lore);
		ItemStack result = new ItemStack(this.icon.getType(), 1);
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		loredump.add(Colors.paint("&fМир: " + location.getWorld().getName()));
		int x = (int) location.getX();
		int y = (int) location.getY();
		int z = (int) location.getZ();
		loredump.add(Colors.paint("&f" + x + " " + y + " " + z));
		loredump.add(Colors.paint("&fВладелец: " + Bukkit.getPlayer(owner).getDisplayName()));
		meta.setLore(loredump);
		result.setItemMeta(meta);
		return result;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return this.name;
	}

	public UUID getOwner() {
		return owner;
	}

	public String getSerializedIcon() {
		Gson gson = new Gson();
		return gson.toJson(icon.serialize());
	}

	public String getSerializedLocation() {
		Gson gson = new Gson();

		return gson.toJson(location.serialize());
	}

	public String getLoreJson() {
		Gson gson = new Gson();
		return gson.toJson(lore);

	}
public Location getLocation() {
	return location;
}

public void setLore(List<String> lore) {
	this.lore = lore;
	
}
public void setIcon(ItemStack icon) {
	this.icon = icon;
   }
}
