package me.gepron1x.maptags.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.gepron1x.maptags.MapTagsPlugin;

public class TabCompleteManager implements TabCompleter {
	private List<String> editparameters;
	private List<String> subcommands;
	MapTagsPlugin main = MapTagsPlugin.getInstance();

	public TabCompleteManager() {
		subcommands = new ArrayList<>();
		editparameters = new ArrayList<>();
		subcommands.add("create");
		subcommands.add("remove");
		subcommands.add("list");
		subcommands.add("share");
		subcommands.add("unshare");
		subcommands.add("help");
		editparameters.add("name");
		editparameters.add("lore");
		editparameters.add("location");
		editparameters.add("icon");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		List<String> result = new ArrayList<>();
		if (args.length > 0 && sender instanceof Player) {
			Player p = (Player) sender;

			switch (args[0]) {
			case "remove":
				if (args.length == 2)
					result = filter(main.getPlayerTagsAsIds(p, false),args[1]);
				break;
			case "share":
			case "unshare":
				if (args.length == 3) {
					result = filter(main.getPlayerTagsAsIds(p, true),args[2]);
				}
				break;
			case "edit":
				if (args.length == 2) {
					result = main.getPlayerTagsAsIds(p, false);
				} else if (args.length == 3) {
					result = filter(editparameters,args[2]);
				}
				break;

			}

		} else if (args.length <= 1 || args[0].equalsIgnoreCase("")) {

			result = subcommands;

		}

		return result;

	}
	private List<String> filter(List<String> inp,String s) {
		if(s == null || s == "") {
			return inp;
		}
		return inp.stream().filter(marker -> marker.startsWith(s)).collect(Collectors.toList());
	}

}
