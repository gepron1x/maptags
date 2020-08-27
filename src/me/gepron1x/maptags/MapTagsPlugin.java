package me.gepron1x.maptags;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.google.common.base.Charsets;

import me.gepron1x.maptags.commands.CommandManager;
import me.gepron1x.maptags.events.InventoryListener;
import me.gepron1x.maptags.utlis.MapTag;

public class MapTagsPlugin extends JavaPlugin {

	private static MapTagsPlugin instance;
	private File customConfig = new File(getDataFolder(), "tags.yml");
	private FileConfiguration mapTags;
	private List<MapTag> maptags = new ArrayList<MapTag>();

	public void onEnable() {
		instance = this;
		getCommand("maptag").setExecutor(new CommandManager());
		mapTags = YamlConfiguration.loadConfiguration(customConfig);
		saveCustomDefaultConfig();
		send("&aPlugin enabled!");
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
	}

	public void onDisable() {
		saveCustomConfig();
		send("&cPlugin deasbled.");
		this.saveConfig();
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
	
	public void addTag(String id, String name, String lore, Player p) {
		ItemStack e;
		if(p.getInventory().getItemInMainHand() != null) {
			e = p.getInventory().getItemInMainHand();
		} else {
		e = new ItemStack(Material.BEDROCK);
		}
		maptags.add(new MapTag(id, name, lore, p.getUniqueId(), p.getLocation(), e));
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

	public List<MapTag> getGlobalList() {
		return maptags;
	}
}
