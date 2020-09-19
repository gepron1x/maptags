package me.gepron1x.maptags.events;

import org.bukkit.Bukkit;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.ConfirmationGUI;
import me.gepron1x.maptags.utlis.GlobalMapTagsGUI;
import me.gepron1x.maptags.utlis.MapTag;

public class InventoryListener implements Listener {

	MapTagsPlugin main = MapTagsPlugin.getInstance();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof GlobalMapTagsGUI) {
	              GlobalGUIHandler(e);
		} else if (e.getClickedInventory().getHolder() instanceof ConfirmationGUI) {
			ConfirmationGUIHandler(e);
		} else {
			return;
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
		main.getWaypoints().addWayPoint((Player) e.getWhoClicked(), tag);
		e.getClickedInventory().setItem(e.getSlot(), gui.getSelected());
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {

			@Override
			public void run() {
				inv.setItem(slot, is);

			}
		}, 20);
			break;
		case SELECT:
			break;
		default:
			break;
		   
		 }
	
	}
private void GlobalGUIHandler(InventoryClickEvent e) {
	GlobalMapTagsGUI gui;
	Player p = (Player) e.getWhoClicked();	
	gui = (GlobalMapTagsGUI) e.getInventory().getHolder();

	
	e.setCancelled(true);
	switch (e.getSlot()) {
	case 46:
		if (gui.getPage() == 0) {
			final Inventory inv = e.getClickedInventory();
			e.getClickedInventory().setItem(46, gui.getNoPage());
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {

				@Override
				public void run() {
					inv.setItem(46, gui.getPrevious());
				}
			}, 20);
			return;
		}
		gui.previous();
		p.closeInventory();
		p.openInventory(gui.getInventory());
		break;
	case 52:

		if (gui.getLastPage() == gui.getPage()) {
			final Inventory inv = e.getClickedInventory();

			e.getClickedInventory().setItem(52, gui.getNoPage());
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {

				@Override
				public void run() {
					inv.setItem(52, gui.getNext());

				}

			}, 20);
			return;
		}
		gui.next();
		p.closeInventory();
		p.openInventory(gui.getInventory());
		break;
	default:
	  executeAction(e);
		break;

	}
	}
private void ConfirmationGUIHandler(InventoryClickEvent e) {
	ConfirmationGUI confirm = (ConfirmationGUI) e.getClickedInventory().getHolder();
	e.setCancelled(true);
	if(e.getCurrentItem().equals(confirm.getYes())) {
		confirm.confirmed();
		e.getWhoClicked().closeInventory();
	} else if(e.getCurrentItem().equals(confirm.getNo())) {
		  e.getWhoClicked().closeInventory();
	} else {
		return;
	}
}
}
