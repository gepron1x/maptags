package me.gepron1x.maptags.utlis;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.google.gson.Gson;

import me.gepron1x.maptags.MapTagsPlugin;

public class MapTag {

	private String id, name;
	private List<String> lore = new ArrayList<String>();
	private UUID owner;
	private Location location;
	private ItemStack icon;
	private boolean isLocal;
    private MapTagsPlugin plugin;
	public MapTag(String id, String name, List<String> lore, UUID owner, Location loc, ItemStack icon,
			boolean isLocal) {
		this.plugin = MapTagsPlugin.getInstance();
		this.isLocal = isLocal;
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
		List<String> loredump = setupLore(plugin.getConfig().getStringList("maptag.lore"));
		ItemStack result = icon;
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(setupName());
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

	public boolean getIsLocal() {
		return isLocal;
	}
   private String getWorldAsString() {
	   for(String wrld : plugin.getConfig().getStringList("maptag.worlds")) {
		   String[] temp = wrld.split(":");
		   String worldid = temp[0];
		   String replacement = temp[1];
		   if(location.getWorld().getName().equals(worldid)) {
			   return Colors.paint(replacement);
		   }
	   }
	return location.getWorld().getName();
	   
   }

	private String getPlayerName() {
		String nick = "";
		Player p = Bukkit.getPlayer(owner);
		if (p == null) {
			nick = Bukkit.getOfflinePlayer(owner).getName();

		} else {
			nick = p.getDisplayName();
		}
		return nick;
	}
private String getStringLocation() {
	Integer x = (int) location.getX();
	Integer y = (int) location.getY();
	Integer z = (int) location.getZ();
	return x + " " + y + " " + z;
}
	private List<String> setupLore(List<String> inp) {
		List<String> result = new ArrayList<>();
		for(String component : inp) {
		 if(component.equals("%lore%")) {
			 result.addAll(Colors.paintList(this.lore));
			 continue;
		 }
			String temp = Colors.paint(component
					.replace("%owner%", getPlayerName())
					.replace("%location%", getStringLocation())
					.replace("%name%", this.name)
					.replace("%world%", getWorldAsString())
					.replace("%id%", this.id));
		      result.add(Colors.paint(temp));
		}
		return result;
	}
private String setupName() {
   for(String nme : plugin.getConfig().getStringList("customcolorname")) {
	   String[] temp = nme.split(":");
	   if(name.contains(temp[0])) return Colors.paint(temp[1]) + name;
   }
	return name;
}
public ItemStack getIcon() {
	return this.icon;
}
}
