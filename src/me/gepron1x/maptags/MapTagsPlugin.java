package me.gepron1x.maptags;

import java.io.File;

import me.gepron1x.maptags.utlis.MySQLWorker;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.base.Charsets;
import me.gepron1x.maptags.commands.CommandManager;
import me.gepron1x.maptags.events.InventoryListener;
import me.gepron1x.maptags.events.WaypointsListener;
import me.gepron1x.maptags.utlis.MapTag;

public class MapTagsPlugin extends JavaPlugin {
	private MySQLWorker mySQL;
	private static MapTagsPlugin instance;
	private File tagsFile = new File(getDataFolder(), "tags.yml");
	private FileConfiguration mapTags;
	WaypointsListener waypoints;
	private File msgFile = new File(getDataFolder(), "messages.yml");
	private FileConfiguration messages;
	private List<MapTag> maptags = new ArrayList<MapTag>();

	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
        waypoints = new WaypointsListener();
        
		mySQL = new MySQLWorker();
		messages = YamlConfiguration.loadConfiguration(msgFile);
		mapTags = YamlConfiguration.loadConfiguration(tagsFile);
		saveCustomDefaultConfig();
		saveDefaultMessages();
		getCommand("maptag").setExecutor(new CommandManager());
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(waypoints, this);
		send("&aPlugin enabled!");
	}

	public void onDisable() {
		savetagsFile();
		reloadMessages();
		reloadConfig();
		send("&cPlugin disabled.");
	}

	public void reloadtagsFile() {
		mapTags = YamlConfiguration.loadConfiguration(tagsFile);
		final InputStream defConfigStream = getResource("tags.yml");
		if (defConfigStream == null) {
			return;
		}
		mapTags.setDefaults(
				YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

	public void savetagsFile() {
		try {
			getConfig().save(tagsFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + tagsFile, ex);
		}
	}

	public void addTag(String id, String name, String lore, Player p, boolean isLocal) {
		if(getGlobalList().stream().anyMatch(marker -> id.equalsIgnoreCase(marker.getId())) == true) {
			p.sendMessage("Метка с этим айди уже существует!");
			return;
		}
		ItemStack e = p.getInventory().getItemInMainHand();
		if (e == null || e.getType() == Material.AIR)
			e = new ItemStack(Material.BEDROCK);
		
		lore = lore.replace('_', ' ');
		name = name.replace('_', ' ');
		
		MapTag tag = new MapTag(id, name, getLoreAsList(lore), p.getUniqueId(), p.getLocation(), e,isLocal);
	    maptags.add(tag);
		mySQL.createMapTag(tag);
	}
public boolean removeTag(String id, Player executor) {
	MapTag tag = getGlobalList().stream().filter(marker -> id.equalsIgnoreCase(marker.getId()) == true).findAny()
			.orElse(null);
	if(tag == null) return false;
	 if(tag.getOwner() != executor.getUniqueId()) {
		 executor.sendMessage("Вам нельзя удалять метки других игроков.");
		 return false;
	 } 
	 
	 getGlobalList().remove(tag);
	 
	 return true;
	
}

	public void saveCustomDefaultConfig() {
		if (!tagsFile.exists()) {
			saveResource("tags.yml", false);
		}
	}

	public void reloadMessages() {
		messages = YamlConfiguration.loadConfiguration(msgFile);
		final InputStream defConfigStream = getResource("messages.yml");
		if (defConfigStream == null) {
			return;
		}
		messages.setDefaults(
				YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

	public void saveMessages() {
		try {
			messages.save(msgFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + msgFile, ex);
		}
	}

	public void saveDefaultMessages() {
		if (!msgFile.exists()) {
			saveResource("messages.yml", false);
		}
	}

	public void send(String s) {
		getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[MapTags] " + s));
	}

	public static MapTagsPlugin getInstance() {
		return instance;
	}

	public List<MapTag> getGlobalList() {
		List<MapTag> global = maptags.stream().filter(marker -> false == marker.getIsLocal()).collect(Collectors.toList());
		return global;
	}
public List<MapTag> getLocalList(UUID player) {
	List<String> perms = getMySQL().getPlayerPermissionsAsList(player);
	
	List<MapTag> local = maptags.stream().
			filter(marker -> true == marker.getIsLocal()).
			collect(Collectors.toList());
	List<MapTag> dump = local.stream().
			filter(marker -> player.equals(marker.getOwner()) || perms.contains(marker.getId())).
			collect(Collectors.toList());
	return dump;
	
}

	public MySQLWorker getMySQL() {
		return mySQL;
	}


	public FileConfiguration getMessages() {
		return messages;
	}

	public void loadTagsfromMySQL(List<MapTag> tags) {
		this.maptags.addAll(tags);
	}
 public WaypointsListener getWaypoints() {
	 return this.waypoints;
 }
public static List<String> getLoreAsList(String lore) {
	List<String> lorear = new ArrayList<String>();
	if (lore.split(";").length != 1) {
		lorear = Arrays.asList(lore.split(";"));
	} else {
		List<String> to = new ArrayList<String>();
		lorear = to;
	}
	return lorear;
}
}
