package me.gepron1x.maptags.utlis.gui;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.MapTag;
import me.gepron1x.maptags.utlis.gui.PlayerListGUI.Share;


public class GlobalMapTagsGUI implements InventoryHolder,MultiPagedInv {
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
	private MapTagsPlugin plugin = MapTagsPlugin.getInstance();
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
public void ClickHandler(InventoryClickEvent e) {
	e.setCancelled(true);
	Player p = (Player) e.getWhoClicked();
	switch (e.getSlot()) {
	case 46:
		if (getPage() == 0) {
			final Inventory inv = e.getClickedInventory();
			e.getClickedInventory().setItem(46, getNoPage());
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					inv.setItem(46, getPrevious());
				}
			}, 20);
			return;
		}
		previous();
		p.closeInventory();
		p.openInventory(getInventory());
		break;
	case 52:

		if (getLastPage() == getPage()) {
			final Inventory inv = e.getClickedInventory();

			e.getClickedInventory().setItem(52, getNoPage());
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

				@Override
				public void run() {
					inv.setItem(52, getNext());

				}

			}, 20);
			return;
		}
		next();
		p.closeInventory();
		p.openInventory(getInventory());
		break;
	default:
	  executeAction(e);
		break;

	}

}
private void executeAction(InventoryClickEvent e) {
	GlobalMapTagsGUI gui = (GlobalMapTagsGUI) e.getClickedInventory().getHolder();
	Player p = (Player) e.getWhoClicked();
	
	 switch(gui.getClickAction()) {
	 case REMOVE:
		p.closeInventory();
		ConfirmationGUI confirm = new ConfirmationGUI(gui.getClickedTag(e.getCurrentItem()));
		p.openInventory(confirm.getInventory());
		break;
	 case WAYPOINT:
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR
			|| e.getCurrentItem().equals(gui.getSelected()))
		return;
	final ItemStack is = e.getCurrentItem();
	final int slot = e.getSlot();
	final Inventory inv = e.getClickedInventory();
	MapTag tag = gui.getClickedTag(is);
	plugin.getWaypoints().addWayPoint((Player) e.getWhoClicked(), tag);
	e.getClickedInventory().setItem(e.getSlot(), gui.getSelected());
	p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
	Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

		@Override
		public void run() {
			inv.setItem(slot, is);

		}
	}, 20);
		break;
	case SELECT:
	   PlayerListGUI plist = new PlayerListGUI("Выберите игрока",gui.getClickedTag(e.getCurrentItem()),Share.SHARE);
	   p.closeInventory();
	   p.openInventory(plist.getInventory());
		break;
	default:
		break;
	   
	 }

}
	
	

}
