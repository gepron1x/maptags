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
	       if(gui.getPage() == 0) {
	    	   e.getClickedInventory().setItem(46, gui.getNoPage());
	    	   return;
	       }
	       
			gui.previous();
			p.closeInventory();
			p.openInventory(gui.getInventory());
			break;
		case 52:
			
			if(gui.getLastPage() == gui.getPage()) {
				e.getClickedInventory().setItem(52, gui.getNoPage());
				return;
			}
			gui.next();
			p.closeInventory();
			p.openInventory(gui.getInventory());
			break;
		}
	}

}
