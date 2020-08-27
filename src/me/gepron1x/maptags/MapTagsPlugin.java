package me.gepron1x.maptags;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import com.google.common.base.Charsets;

public class MapTagsPlugin extends JavaPlugin {

	private static MapTagsPlugin instance;
	private File customConfig = new File(getDataFolder(), "tags.yml");
	private FileConfiguration mapTags;
	private GlobalTagList globallist;

	public void onEnable() {
		instance = this;
		getCommand("maptag").setExecutor(new mapTagCmd());
		mapTags = YamlConfiguration.loadConfiguration(customConfig);
		saveCustomDefaultConfig();
		send("&aПлагин включен, вроде бы");
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
	}

	public void onDisable() {
		saveCustomConfig();
		send("&cПлагин выключен.");
		this.saveConfig();
	}

	public GlobalTagList getGloballist() {
		return globallist;
	}

	public void reloadCustomConfig() {
		mapTags = YamlConfiguration.loadConfiguration(customConfig);
		final InputStream defConfigStream = getResource("tags.yml");
		if (defConfigStream == null) {
			return;
		}

		mapTags.setDefaults(
				YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

	public void saveCustomConfig() {
		try {
			getConfig().save(customConfig);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + customConfig, ex);
		}
	}

	public void saveCustomDefaultConfig() {
		if (!customConfig.exists()) {
			saveResource("tags.yml", false);
		}
	}

	public void send(String s) {
		getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[MapTags] " + s));
	}

	public static MapTagsPlugin getInstance() {
		return instance;
	}
}
