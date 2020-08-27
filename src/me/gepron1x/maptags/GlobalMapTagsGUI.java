package me.gepron1x.maptags;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GlobalMapTagsGUI implements InventoryHolder {

	private List<Inventory> pages;
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private int lastpage = 1;
	private int page = 0;
	private int lastslot = 0;
	private Inventory openedPage;

	public GlobalMapTagsGUI(List<MapTag> items) {
		this.maptags = items;
		this.pages = new ArrayList<Inventory>();
		build();
	}

	public Inventory getInventory() {
		openedPage = pages.get(page);
		return openedPage;
	}

	private void build() {
		Inventory inv = Bukkit.createInventory(this, 6 * 9, "Глобальные метки #" + lastpage);
		pages.clear();
		for (MapTag tag : maptags) {
			inv.setItem(lastslot, tag.toItemStack());
			lastslot++;
			if (lastslot == 45) {
				inv.setItem(46, new ItemStack(Material.ARROW));
				inv.setItem(52, new ItemStack(Material.ARROW));
				pages.add(inv);
				lastpage++;
				inv = Bukkit.createInventory(this, 6 * 9, "Глобальные метки #" + lastpage);
				lastslot = 0;
			}
		}
		inv.setItem(46, new ItemStack(Material.ARROW));
		inv.setItem(52, new ItemStack(Material.ARROW));
		pages.add(inv);
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void next() {
		page++;
	}

	public void previous() {
		page--;
	}
}
