package me.gepron1x.maptags;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MapTagsPlugin extends JavaPlugin {
	
	File MapTagsStorage;
	public FileConfiguration maptags;
	private GlobalTagList globallist;

	public void onEnable() {

		MapTagsStorage = new File(getDataFolder(), "tags.yml");
		// set the file location
		maptags = YamlConfiguration.loadConfiguration(MapTagsStorage);
		this.getConfig().options().copyDefaults(true);
		this.maptags.options().copyDefaults(true);
		this.getLogger().info("set text!");

		this.saveConfigs();

	}

	public void saveConfigs() {
		try {
			maptags.save(MapTagsStorage);
			this.saveConfig();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void onDisable() {
		this.getLogger().info("set text!");
		this.saveConfigs();
	}

	public GlobalTagList getGloballist() {
		return globallist;
	}

}
