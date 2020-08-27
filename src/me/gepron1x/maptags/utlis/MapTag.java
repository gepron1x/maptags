package me.gepron1x.maptags.utlis;

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
		if (lore.split(";").length != 1) {
			this.lore = Arrays.asList(lore.split(";"));
		} else {
			List<String> to = new ArrayList<String>();
			to.add(lore);
			this.lore = to;
		}
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
		ItemStack result = new ItemStack(this.icon.getType(), this.icon.getAmount());
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		lore.add("Enter text: " + location.getWorld().getName());
		int x = (int) location.getX();
		int y = (int) location.getY();
		int z = (int) location.getZ();
		lore.add(x + " " + y + " " + z);
		lore.add("Enter text: " + Bukkit.getPlayer(owner).getDisplayName());
		meta.setLore(lore);
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
