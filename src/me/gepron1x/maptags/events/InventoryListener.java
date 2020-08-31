package me.gepron1x.maptags.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.GlobalMapTagsGUI;

public class InventoryListener implements Listener {

	GlobalMapTagsGUI gui;
	MapTagsPlugin main = MapTagsPlugin.getInstance();

	@EventHandler
	public void onClick(InventoryClickEvent e) {

		if (e.getInventory().getHolder() instanceof GlobalMapTagsGUI) {
			gui = (GlobalMapTagsGUI) e.getInventory().getHolder();
		} else {
			return;
		}
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
						inv.setItem(46, new ItemStack(Material.ARROW));
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
						inv.setItem(52, new ItemStack(Material.ARROW));

					}

				}, 20);
				return;
			}
			gui.next();
			p.closeInventory();
			p.openInventory(gui.getInventory());
			break;
	   default:
		   gui.getClickedTag(e.getCurrentItem());
		   
		
		}
	}

}
