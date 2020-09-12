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
	private String shared,unshared,unselected,notOwner,playeronly,tagnotexists,notinownregion;
	
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
				sender.sendMessage(unselected);
				break;
			case "edit":
				editCommand(sender, args);
				break;
			case "help":
				throwHelp(sender);
				break;
			case "reload":
				main.reload();
				break;
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
        
	}

	private void removeCommand(CommandSender sender, String[] args) {
	main.removeTag(args[1], (Player) sender);
	}
	private void listCommand(CommandSender sender, String[] args) {
		GlobalMapTagsGUI gui;
		Player p = (Player) sender;
		if (args.length == 2  && args[1].equalsIgnoreCase("local")) {
			gui = new GlobalMapTagsGUI(main.getLocalList(p.getUniqueId()), main.getConfig().getString("gui.list.title.local"));

		} else {
			gui = new GlobalMapTagsGUI(main.getGlobalList(), main.getConfig().getString("gui.list.title.global"));
		}
		p.openInventory(gui.getInventory());

	}

	@SuppressWarnings("deprecation")
	private void shareCommand(CommandSender sender, String[] args) {
		Player pl = Bukkit.getPlayer(args[1]);
		UUID playertoshare;
		if(pl == null) {
			playertoshare = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
		} else {
			playertoshare = pl.getUniqueId();
		}
		Player p = (Player) sender;
		String id = args[2];
		MapTag tag = main.getGlobalList().stream().filter(marker -> id.equalsIgnoreCase(marker.getId())).findAny()
				.orElse(null);
		if (tag == null) {
			sender.sendMessage(tagnotexists);
			return;
		}

		if (!tag.getOwner().equals(p.getUniqueId())) {
			sender.sendMessage(notOwner);
			return;
		}
		;
		if (args[0].equalsIgnoreCase("share")) {
			main.getMySQL().setPlayerPermission(playertoshare, id);
			if(pl != null) pl.sendMessage(shared);
		} else if (args[0].equalsIgnoreCase("unshare")) {
			main.getMySQL().removePermission(playertoshare, id);
			if(pl != null) pl.sendMessage(unshared);
		}
	}

	private void editCommand(CommandSender sender, String[] args) {
		Gson gson = new Gson();
		String object = "";
		MapTag tag = main.getGlobalList().stream().filter(marker -> args[1].equals(marker.getId()))
				.findAny().orElse(null);
		if (tag == null) {
			sender.sendMessage(tagnotexists);
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
				if(!main.checkForRegion(p)) {
					p.sendMessage(notinownregion);
					return;
				}
				Location location = p.getLocation();
				tag.setLocation(location);
				object = gson.toJson(location.serialize());
			case "icon":
				ItemStack is = Colors.buildIcon(p.getInventory().getItemInMainHand());
				tag.setIcon(is);
				object = gson.toJson(is.serialize());
				break;
			default:
				sender.sendMessage(Colors.paint(main.getMessages().getString("command.edit.parameternotexists")));
				return;
			}
		    main.editTag(tag, index);
			main.getMySQL().editMapTag(tag.getId(), args[2], object);
			sender.sendMessage(Colors.paint(main.getMessages().getString("command.edit."+args[2])).replace("%argument%", args[3]));

		} else {
			sender.sendMessage(notOwner);
		}

	}
public void reloadMessages() {
	this.notinownregion = Colors.paint(main.getMessages().getString("worldguard.not-in-own-region"));
	this.playeronly = Colors.paint(main.getMessages().getString("command.player-only"));
	this.notOwner = Colors.paint(main.getMessages().getString("command.notowner"));
	this.shared = Colors.paint(main.getMessages().getString("command.shared"));
	this.unshared = Colors.paint(main.getMessages().getString("command.unshared"));
	this.unselected = Colors.paint(main.getMessages().getString("command.unselected"));
	this.tagnotexists = Colors.paint(main.getMessages().getString("command.notexists"));
}



}
