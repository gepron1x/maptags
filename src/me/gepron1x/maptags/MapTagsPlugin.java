package me.gepron1x.maptags;

import java.io.File;
import me.gepron1x.maptags.utlis.MySQLWorker;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
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
import me.gepron1x.maptags.utlis.MapTag;

public class MapTagsPlugin extends JavaPlugin {
	private MySQLWorker mySQL;
	private static MapTagsPlugin instance;
	private File tagsFile = new File(getDataFolder(), "tags.yml");
	private FileConfiguration mapTags;
	private File msgFile = new File(getDataFolder(), "messages.yml");
	private FileConfiguration messages;
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private HashMap<UUID, MapTag> selectedTags = new HashMap<UUID, MapTag>();

	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();
		getCommand("maptag").setExecutor(new CommandManager());
		mySQL = new MySQLWorker();
		messages = YamlConfiguration.loadConfiguration(msgFile);
		mapTags = YamlConfiguration.loadConfiguration(tagsFile);
		saveCustomDefaultConfig();
		saveDefaultMessages();
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		send("&aPlugin enabled!");
	}

	public void onDisable() {
		savetagsFile();
		reloadMessages();
		reloadConfig();
		send("&cPlugin deasbled.");
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

	public void addTag(String id, String name, String lore, Player p) {
		ItemStack e;
		if (p.getInventory().getItemInMainHand() != null) {
			e = p.getInventory().getItemInMainHand();
		} else {
			e = new ItemStack(Material.BEDROCK);
		}
		maptags.add(new MapTag(id, name, lore, p.getUniqueId(), p.getLocation(), e));
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
		return maptags;
	}

	public MySQLWorker getMySQL() {
		return mySQL;
	}

	public HashMap<UUID, MapTag> getSelectedTags() {
		return selectedTags;
	}

	public FileConfiguration getMessages() {
		return messages;
	}
}
