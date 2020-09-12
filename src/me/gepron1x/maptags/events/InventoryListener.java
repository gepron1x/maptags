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
import me.gepron1x.maptags.utlis.GlobalMapTagsGUI;
import me.gepron1x.maptags.utlis.MainGUI;
import me.gepron1x.maptags.utlis.MapTag;

public class InventoryListener implements Listener {

	MapTagsPlugin main = MapTagsPlugin.getInstance();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		GlobalMapTagsGUI gui;
		if (e.getInventory().getHolder() instanceof GlobalMapTagsGUI) {
			gui = (GlobalMapTagsGUI) e.getInventory().getHolder();

			Player p = (Player) e.getWhoClicked();
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
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().equals(gui.getSelected()))
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

			}
		} else if (e.getClickedInventory().getHolder() instanceof MainGUI) {
			e.setCancelled(true);
			MainGUI maingui = (MainGUI) e.getClickedInventory().getHolder();
			if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem() == null)
				return;
			maingui.executeAction(e.getSlot(), (Player) e.getWhoClicked());

		} else {
			return;
		}
	}

}
