package me.gepron1x.maptags.commands;

import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.GlobalMapTagsGUI;
import me.gepron1x.maptags.utlis.MapTag;

public class CommandManager implements CommandExecutor {

	public MapTagsPlugin main = MapTagsPlugin.getInstance();
	String created;
	String removed;
	String shared;
	String unshared;
	String unselected;
	String notOwner;
	String playeronly;
     public CommandManager() {
    	 reloadMessages();
     }
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length == 0)
			return true;
		if (sender instanceof Player) {
			switch (args[0]) {
			case "create":
				createCommand(sender, args);
				break;
			case "remove":
				removeCommand(sender, args);
				break;
			case "list":
				listCommand(sender, args);
				break;
			case "share":
			case "unshare":
				shareCommand(sender, args);
				break;
			case "unselect":
				main.getWaypoints().removeWayPoint((Player) sender);
				break;
			case "edit":
				editCommand(sender, args);
				break;
			case "help":
				throwHelp(sender);
			case "debug":
				for(MapTag tag : main.getGlobalList()) {
					sender.sendMessage(tag.getId());
				}
				sender.sendMessage(main.getMessages().getString("command.player-only"));
				break;
			case "reload":
				main.reload();
			default:
				throwInfo(sender);
				break;
			}
			return true;
		} else {
			sender.sendMessage(playeronly);
		}
		return true;
	}

	private void throwHelp(CommandSender sender) {
		for (String msg : main.getMessages().getStringList("command.help")) {
			sender.sendMessage(Colors.paint(msg));
		}

	}

	private void throwInfo(CommandSender sender) {
		for (String msg : main.getMessages().getStringList("command.info")) {
			sender.sendMessage(Colors.paint(msg));
		}
	}

	private void createCommand(CommandSender sender, String[] args) {
		boolean isLocal = false;
		Player p = (Player) sender;

		if (args[4].equalsIgnoreCase("local"))
			isLocal = true;
		main.addTag(args[1], args[2], args[3], p, isLocal);
        sender.sendMessage(this.created.replace("%name%", Colors.paint(args[2])));
	}

	private void removeCommand(CommandSender sender, String[] args) {
		boolean isOk = main.removeTag(args[1], (Player) sender);
		if (isOk == true)
			sender.sendMessage(removed);
	}

	private void listCommand(CommandSender sender, String[] args) {
		GlobalMapTagsGUI gui;
		Player p = (Player) sender;
		if (args.length == 2  && args[1].equalsIgnoreCase("local")) {
			gui = new GlobalMapTagsGUI(main.getLocalList(p.getUniqueId()), "Локальные метки №");

		} else {
			gui = new GlobalMapTagsGUI(main.getGlobalList(), "Глобальные метки №");
		}
		p.openInventory(gui.getInventory());

	}

	@SuppressWarnings("deprecation")
	private void shareCommand(CommandSender sender, String[] args) {
		UUID playertoshare = Bukkit.getPlayer(args[1]).getUniqueId();
		if(playertoshare == null) {
			playertoshare = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
		}
		Player p = (Player) sender;
		String id = args[2];
		MapTag tag = main.getGlobalList().stream().filter(marker -> id.equalsIgnoreCase(marker.getId())).findAny()
				.orElse(null);
		if (tag == null) {
			sender.sendMessage("Такой метки либо не существует, либо она глобальная.");
			return;
		}

		if (!tag.getOwner().equals(p.getUniqueId())) {
			sender.sendMessage(notOwner);
			return;
		}
		;
		if (args[0].equalsIgnoreCase("share")) {
			main.getMySQL().setPlayerPermission(playertoshare, id);
		} else if (args[0].equalsIgnoreCase("unshare")) {
			main.getMySQL().removePermission(playertoshare, id);
		}
	}

	private void editCommand(CommandSender sender, String[] args) {
		Gson gson = new Gson();
		String object = "";
		MapTag tag = main.getGlobalList().stream().filter(marker -> args[1].equals(marker.getId()))
				.findAny().orElse(null);
		if (tag == null) {
			sender.sendMessage("Извини, но такой метки не существует. :)");
			return;
		}
		Player p = (Player) sender;
		int index = main.getGlobalList().lastIndexOf(tag);
		if (tag.getOwner().equals(p.getUniqueId())) {
			switch (args[2]) {
			case "name":
				String name = Colors.buildName(args[3]);
				tag.setName(name);
				object = name;

				break;
			case "lore":
				String lore = args[3];
				List<String> loredump = Colors.stringAsList(lore);
				tag.setLore(loredump);
				object = gson.toJson(loredump);
				break;
			case "location":
				Location location = p.getLocation();
				tag.setLocation(location);
				object = gson.toJson(location.serialize());
			case "icon":
				ItemStack is = Colors.buildIcon(p.getInventory().getItemInMainHand());
				tag.setIcon(is);
				object = gson.toJson(is.serialize());
				break;
			default:
				sender.sendMessage("Извини, но такого параметра не существует :)");
				return;
			}
		    main.editTag(tag, index);
			main.getMySQL().editMapTag(tag.getId(), args[2], object);

		} else {
			sender.sendMessage(notOwner);
		}

	}
public void reloadMessages() {
	this.playeronly = Colors.paint(main.getMessages().getString("command.player-only"));
	this.notOwner = Colors.paint(main.getMessages().getString("command.notowner"));
	this.created = Colors.paint(main.getMessages().getString("command.created"));
	this.removed = Colors.paint(main.getMessages().getString("command.removed"));
	this.shared = Colors.paint(main.getMessages().getString("command.shared"));
	this.unshared = Colors.paint(main.getMessages().getString("command.unshared"));
	this.unselected = Colors.paint(main.getMessages().getString("command.unselected"));
}

}
