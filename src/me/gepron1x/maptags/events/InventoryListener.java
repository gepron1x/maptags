package me.gepron1x.maptags.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.MapTagBuilder;
import me.gepron1x.maptags.utlis.gui.ConfirmationGUI;
import me.gepron1x.maptags.utlis.gui.GlobalMapTagsGUI;
import me.gepron1x.maptags.utlis.gui.PlayerListGUI;

public class InventoryListener implements Listener {

	MapTagsPlugin main = MapTagsPlugin.getInstance();

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (holder instanceof GlobalMapTagsGUI) {
			GlobalMapTagsGUI gui = (GlobalMapTagsGUI) holder;
			gui.ClickHandler(e);
		} else if (holder instanceof ConfirmationGUI) {
			ConfirmationGUI gui = (ConfirmationGUI) holder;
			gui.ClickHandler(e);
		} else if (holder instanceof MapTagBuilder) {
			MapTagBuilder builder = (MapTagBuilder) holder;
			builder.setIsLocal(e);
		} else if(holder instanceof PlayerListGUI) {
			 PlayerListGUI gui = (PlayerListGUI) holder;
			 gui.ClickHandler(e);
		}
			else {
				return;
		}
			
		}

	}


