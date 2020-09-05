package me.gepron1x.maptags.commands;

import org.bukkit.Bukkit;
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
		if(args.length == 0) return true;
		switch(args[0]) {
		case "create":
	     createCommand(sender,args);
	     break;
		case "remove":
		 removeCommand(sender,args);
		}
		return true;
	}

	private void throwHelp(CommandSender sender) {
		// TODO Auto-generated method stub

	}

	private void throwInfo(CommandSender sender) {

	}
	private void createCommand(CommandSender sender,String[] args) {
		if(sender instanceof Player) {
			boolean isLocal = false;
		Player p = (Player) sender;
		if(args[4].equalsIgnoreCase("local")) isLocal = true;
		   main.addTag(args[1], args[2], args[3], p, isLocal);
		} else {
			sender.sendMessage(Colors.paint(main.getMessages().getString("players-only")));
		}
	}
   private void removeCommand(CommandSender sender,String[] args) {
	    boolean isOk = main.removeTag(args[1], (Player) sender);
	    if(isOk == true) sender.sendMessage("Метка успешно удалена.");
   }

}
