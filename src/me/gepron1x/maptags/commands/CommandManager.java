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
			  editCommand(sender,args);
			  break;
			}
			return true;
		} else {
			sender.sendMessage(Colors.paint(main.getMessages().getString("command.players-only")));
		}
		return true;
	}

	private void throwHelp(CommandSender sender) {
		// TODO Auto-generated method stub

	}

	private void throwInfo(CommandSender sender) {

	}

	private void createCommand(CommandSender sender, String[] args) {
		boolean isLocal = false;
		Player p = (Player) sender;
		if (args[4].equalsIgnoreCase("local"))
			isLocal = true;
		main.addTag(args[1], args[2], args[3], p, isLocal);

	}

	private void removeCommand(CommandSender sender, String[] args) {
		boolean isOk = main.removeTag(args[1], (Player) sender);
		if (isOk == true)
			sender.sendMessage("Метка успешно удалена.");
	}

	private void listCommand(CommandSender sender, String[] args) {
		GlobalMapTagsGUI gui;
		Player p = (Player) sender;
		if (args[1].equalsIgnoreCase("local")) {
			gui = new GlobalMapTagsGUI(main.getLocalList(p.getUniqueId()), "Локальные метки №");

		} else {
			gui = new GlobalMapTagsGUI(main.getGlobalList(), "Глобальные метки №");
		}
		p.openInventory(gui.getInventory());

	}

	private void shareCommand(CommandSender sender, String[] args) {
		UUID p = Bukkit.getPlayer(args[1]).getUniqueId();
		String id = args[2];
		if (args[0].equalsIgnoreCase("share")) {
			main.getMySQL().setPlayerPermission(p, id);
		} else if (args[0].equalsIgnoreCase("unshare")) {
			main.getMySQL().removePermission(p, id);
		}
	}
	private void editCommand(CommandSender sender,String[] args) {
		Gson gson = new Gson();
		boolean isLocal = false;
		String object = "";
		MapTag tag = main.getGlobalList().stream().filter(marker -> args[1].equalsIgnoreCase(marker.getId()) == true).findAny()
				.orElse(null);
		if(tag == null) {
		tag = main.getMySQL().getMapTag(args[1]);
		isLocal = true;
		}
		if(tag == null) {
			sender.sendMessage("Извини, но такой метки не существует. :)");
			return;
		}
		Player p = (Player) sender;
		int index = main.getGlobalList().lastIndexOf(tag);
		if(tag.getOwner().equals(p.getUniqueId())) {
			switch(args[2]) {
			case "name":
			 tag.setName(args[3]);
			 object = args[3];
			
			break;
			 case "lore":
				String lore = args[3].replace('_', ' ');
				List<String> loredump = MapTagsPlugin.getLoreAsList(lore);
			  tag.setLore(loredump);
			  object = gson.toJson(loredump);
			 break;
			 case "location":
				Location location = p.getLocation();
				tag.setLocation(location);
			 object = gson.toJson(location.serialize());
			 case "icon":
				 ItemStack is = p.getInventory().getItemInMainHand();
				 tag.setIcon(is);
				object = gson.toJson(is.serialize());
				 break;
			 default: 
				 sender.sendMessage("Извини, но такого параметра не существует :)");
				 return;
			}
			if(isLocal == false) {
				main.getGlobalList().set(index, tag);
			}
			main.getMySQL().editMapTag(tag.getId(), args[2], object);
	
			
		}
		
		
	}

}
