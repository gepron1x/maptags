package me.gepron1x.maptags.utlis;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class GlobalMapTagsGUI implements InventoryHolder {
	private List<Inventory> pages;
	Map<ItemStack, MapTag> clickables;
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private int lastpage = 0;
	private int page = 0;
	private int lastslot = 0;
	private Inventory openedPage;
	private ItemStack nopage;
	private String title;
	private ItemStack nextPage;
	private ItemStack previousPage;
	private ItemStack selected;
	private ClickAction action;
public enum ClickAction {
	WAYPOINT,
	REMOVE,
	SELECT;
}

	public GlobalMapTagsGUI(List<MapTag> items, String title, ClickAction action) {
		this.action = action;
		updateSetup();
		this.maptags = items;
		this.clickables = new HashMap<ItemStack, MapTag>();
		this.title = title;
		this.pages = new ArrayList<Inventory>();
		build();
	}

	public Inventory getInventory() {
		openedPage = pages.get(page);
		return openedPage;
	}

	private void build() {
		int i = lastpage + 1;
		Integer pg = i;
		Inventory inv = Bukkit.createInventory(this, 6 * 9, title.replace("%page%", pg.toString()));
		pages.clear();
		for (MapTag tag : maptags) {
			ItemStack ist = tag.toItemStack();
			inv.setItem(lastslot, ist);
			clickables.put(ist, tag);
			lastslot++;
			if (lastslot == 45) {
				inv.setItem(46, previousPage);
				inv.setItem(52, nextPage);
				pages.add(inv);
				lastpage++;
				i = lastpage + 1;
				inv = Bukkit.createInventory(this, 6 * 9, title.replace("%page%", pg.toString()));
				lastslot = 0;
			}
		}
		inv.setItem(46, previousPage);
		inv.setItem(52, nextPage);
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

	public ItemStack getSelected() {
		return selected;
	}

	public ItemStack getPrevious() {
		return previousPage;
	}

	public ItemStack getNext() {
		return nextPage;
	}

	public MapTag getClickedTag(ItemStack icon) {
		return clickables.get(icon);

	}
	public ClickAction getClickAction() {
		return action;
		
	}

	public void updateSetup() {
		this.nopage = Colors.buildItemStackFromConfig("gui.list.nopage");
		this.nextPage = Colors.buildItemStackFromConfig("gui.list.nextPage");
		this.previousPage = Colors.buildItemStackFromConfig("gui.list.previousPage");
		this.selected = Colors.buildItemStackFromConfig("gui.list.selected");
	}
	

}
