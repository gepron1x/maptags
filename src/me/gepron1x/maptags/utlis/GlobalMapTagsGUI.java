package me.gepron1x.maptags.utlis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlobalMapTagsGUI implements InventoryHolder {

	private List<Inventory> pages;
	Map<ItemStack,MapTag> clickables;
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private int lastpage = 0;
	private int page = 0;
	private int lastslot = 0;
	private Inventory openedPage;
	private ItemStack nopage;
	private String title;

	public GlobalMapTagsGUI(List<MapTag> items,String title) {
		this.maptags = items;
		this.clickables = new HashMap<ItemStack,MapTag>();
		this.title = title;
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
		int i = lastpage+1;
		Inventory inv = Bukkit.createInventory(this, 6 * 9, title + i);
		pages.clear();
		for (MapTag tag : maptags) {
			ItemStack ist = tag.toItemStack();
			inv.setItem(lastslot, ist);
			clickables.put(ist, tag);
			lastslot++;
			if (lastslot == 45) {
				inv.setItem(46, new ItemStack(Material.ARROW));
				inv.setItem(52, new ItemStack(Material.ARROW));
				pages.add(inv);
				lastpage++;
				i = lastpage+1;
				inv = Bukkit.createInventory(this, 6 * 9, title + i);
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
		return lastpage;
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
public MapTag getClickedTag(ItemStack icon) {
	return clickables.get(icon);
	
    }
}
