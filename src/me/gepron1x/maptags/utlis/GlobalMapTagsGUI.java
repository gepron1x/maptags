package me.gepron1x.maptags.utlis;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlobalMapTagsGUI implements InventoryHolder {

	private List<Inventory> pages;
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private int lastpage = 1;
	private int page = 0;
	private int lastslot = 0;
	private Inventory openedPage;
	private ItemStack nopage;

	public GlobalMapTagsGUI(List<MapTag> items) {
		this.maptags = items;
		this.pages = new ArrayList<Inventory>();
		this.nopage = new ItemStack(Material.BARRIER);
		ItemMeta meta = nopage.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Следующей страницы нет!");
		nopage.setItemMeta(meta);
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

	public int getPage() {
		return page;
	}

	public int getLastPage() {
		return page;
	}

	public void next() {
		page++;
	}

	public void previous() {
		page--;
	}

	public ItemStack getNoPage() {
		return nopage;
	}
}
