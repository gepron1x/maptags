package me.gepron1x.maptags.utlis;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gepron1x.maptags.MapTagsPlugin;

public class MainGUI implements InventoryHolder {
	private Inventory gui;
	private MapTagsPlugin main;
	private HashMap<Integer, String> actions = new HashMap<Integer, String>();

	public MainGUI() {
		main = MapTagsPlugin.getInstance();
		String title = main.getConfig().getString("gui.main");
		int size = main.getConfig().getInt("gui.main.rows") * 9;
		gui = Bukkit.createInventory(this, size, title);
		for (String path : main.getConfig().getConfigurationSection("gui.main").getKeys(false)) {
			String name = main.getConfig().getString("gui.main." + path + ".name");
			List<String> lore = main.getConfig().getStringList("gui.main." + path + ".lore");
			ItemStack icon = new ItemStack(
					Material.getMaterial(main.getConfig().getString("gui.main." + path + ".material")),
					main.getConfig().getInt("gui.main." + path + ".count"));
			ItemMeta meta = icon.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			icon.setItemMeta(meta);
			int slot = main.getConfig().getInt("gui.main." + path + ".slot");
			gui.setItem(slot, icon);
			actions.put(slot, main.getConfig().getString("gui.main." + path + "action"));

		}
	}

	@Override
	public Inventory getInventory() {
		// TODO Автоматически созданная заглушка метода
		return gui;
	}

	public void executeAction(int slot, Player p) {
		String action = actions.get(slot);
		switch (action) {
		case "OPEN_GLOBAL_TAGS":
			GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getGlobalList(), "Глобальные метки");
			p.closeInventory();
			p.openInventory(gui.getInventory());
			break;
		case "OPEN_LOCAL_TAGS":
			GlobalMapTagsGUI local = new GlobalMapTagsGUI(main.getMySQL().getPlayerMapTags(p.getUniqueId()),
					"Локальные метки");
			p.closeInventory();
			p.openInventory(local.getInventory());
			break;
		}
	}
}
