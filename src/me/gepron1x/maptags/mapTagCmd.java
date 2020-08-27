package me.gepron1x.maptags;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mapTagCmd implements CommandExecutor {

	public MapTagsPlugin main = MapTagsPlugin.getInstance();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		switch (args[0]) {
		case "create":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				main.getGloballist().addTag(args[1], args[2], args[3], p);
				return true;
			}
			break;
		case "list":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getGloballist().getList());
				p.openInventory(gui.getInventory());
				return true;
			}
		case "textlist":
			List<MapTag> maptags = main.getGloballist().getList();
			for(MapTag tag : maptags) {
				sender.sendMessage(tag.getName());
			}
		}
		return true;
	}

}
