package me.gepron1x.maptags;

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
				main.addTag(args[1], args[2], args[3], p);
				return true;
			}
			break;
		case "list":
			if (sender instanceof Player) {
				Player p = (Player) sender;
				GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getGlobalList());
				p.openInventory(gui.getInventory());
				return true;
			}
			break;
		case "debug":
			for (MapTag tag : main.getGlobalList()) {
				sender.sendMessage(tag.getName() + " " + tag.getId());
			}
		}
		return true;
	}

}
