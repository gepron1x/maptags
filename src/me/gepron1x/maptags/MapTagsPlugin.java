package me.gepron1x.maptags;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

public class MapTagsPlugin extends JavaPlugin {
	private File customconfig = new File(getDataFolder(), "tags.yml");
	private FileConfiguration maptags;
	private GlobalTagList globallist;

	public void onEnable() {
		this.saveDefaultCustomConfig();
		this.saveDefaultConfig();

		this.getLogger().info("Плагин включен!");

		this.saveConfigs();

	}

	public void saveConfigs() {
		try {
			maptags.save(customconfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
		this.getLogger().info("Плагин отключен!");
		this.saveConfigs();
	}

	public void saveDefaultCustomConfig() {
		if (!this.customconfig.exists())
			saveResource("tags.yml", false);
		maptags = YamlConfiguration.loadConfiguration(customconfig);
	}

	public void saveCustomConfig() {
		try {
			maptags.save(this.customconfig);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + this.customconfig, ex);
		}
	}

	public void reloadCustomConfig() {
		this.maptags = (FileConfiguration) YamlConfiguration.loadConfiguration(this.customconfig);
		InputStream defConfigStream = getResource("tags.yml");
		if (defConfigStream == null)
			return;
		this.maptags.setDefaults((Configuration) YamlConfiguration
				.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
	}

}
