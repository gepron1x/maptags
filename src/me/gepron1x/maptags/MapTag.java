package me.gepron1x.maptags;

import java.util.ArrayList;
import java.util.Arrays;
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

	public MapTag(String id, String name, String lore, UUID owner, Location loc, ItemStack icon) {
		this.id = id;
		this.name = name;
		this.lore = Arrays.asList(lore.split(";"));
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

	public void setLore(String lore) {
		this.lore = Arrays.asList(lore.split(";"));
	}

	public ItemStack toItemStack() {
		ItemStack result = this.icon;
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		lore.add("Enter text: " + location.getWorld().toString());
		int x = (int) location.getX();
		int y = (int) location.getY();
		int z = (int) location.getZ();
		lore.add(x + " " + y + " " + z);
		lore.add("Enter text: " + Bukkit.getPlayer(owner).getDisplayName());
		meta.setLore(lore);
		return icon;
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

	public String getIcon() {
		Gson gson = new Gson();
		return gson.toJson(icon);
	}

	public String getLocation() {
		String result = location.getWorld().toString() + "," + location.getX() + "," + location.getY() + ","
				+ location.getZ();
		return result;
	}
}
