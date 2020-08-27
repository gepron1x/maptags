package me.gepron1x.maptags;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class mapTagCmd implements CommandExecutor {
	public MapTagsPlugin main = MapTagsPlugin.getInstance();
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Автоматически созданная заглушка метода
		switch(args[0]) {
		case "list":
		 main.getGloballist();
		}
		return true;
	}

}
