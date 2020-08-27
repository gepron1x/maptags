package me.gepron1x.maptags;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
	
	GlobalMapTagsGUI gui;
	
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
			gui.previous();
			p.updateInventory();
			break;
		case 52:
			gui.next();
			p.updateInventory();
			break;
		}
	}

}
