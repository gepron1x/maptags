package me.gepron1x.maptags.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.GlobalMapTagsGUI;
import me.gepron1x.maptags.utlis.MapTag;

public class CommandManager implements CommandExecutor {

	public MapTagsPlugin main = MapTagsPlugin.getInstance();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					throwHelp(sender);
				} else if (args[0].equalsIgnoreCase("info")) {
					throwInfo(sender);
				} else if (args[0].equalsIgnoreCase("unselect")) {
					if (sender instanceof Player) {
						// unselect
					} else {
						sender.sendMessage(Colors.paint(main.getConfig().getString("player-only")));
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getGlobalList());
						p.openInventory(gui.getInventory());
						return true;
					} else {
						sender.sendMessage(Colors.paint(main.getConfig().getString("player-only")));
					}
				} else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("select")) {
					if (sender instanceof Player) {
						// select tag for compass
					} else {
						sender.sendMessage(Colors.paint(main.getConfig().getString("player-only")));
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					for (MapTag tag : main.getGlobalList()) {
						if (tag.getId().equalsIgnoreCase(args[1])) {
							main.getGlobalList().remove(tag);
							break;
						}
					}
					//send message removed
				} else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("create")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						main.addTag(args[1], args[2], args[3], p);
						//send message created
						return true;
					} else {
						sender.sendMessage(Colors.paint(main.getConfig().getString("player-only")));
					}
				} else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else {
				sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
			}
		} else {
			throwInfo(sender);
		}
		return true;
	}

	private void throwHelp(CommandSender sender) {
		// TODO Auto-generated method stub

	}

	private void throwInfo(CommandSender sender) {
		// TODO Auto-generated method stub

	}

}
