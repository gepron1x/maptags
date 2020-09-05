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
		if (args.length > 0) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("help")) {
					throwHelp(sender);
				} else if (args[0].equalsIgnoreCase("info")) {
					throwInfo(sender);
				} else if (args[0].equalsIgnoreCase("debug")) {
					if(sender instanceof Player) {
						Player p = (Player) sender;
						  sender.sendMessage(main.getMySQL().getPlayerPermissions(p.getUniqueId()));
					}
                       
				} else if (args[0].equalsIgnoreCase("unselect")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						main.getWaypoints().removeWayPoint(p);
					} else {
						sender.sendMessage(Colors.paint(main.getMessages().getString("player-only")));
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getGlobalList(),"Глобальные метки #");
						p.openInventory(gui.getInventory());
						return true;
					} else {
						sender.sendMessage(Colors.paint(main.getMessages().getString("player-only")));
					}
				} else if (args[0].equalsIgnoreCase("locallist")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						GlobalMapTagsGUI gui = new GlobalMapTagsGUI(main.getMySQL().getPlayerMapTags(p.getUniqueId()),"Локальные метки игрока "+p.getDisplayName()+" #");
						p.openInventory(gui.getInventory());
						return true;
					} else {
						sender.sendMessage(Colors.paint(main.getMessages().getString("player-only")));
					}
				}

				else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("select")) {
					if (sender instanceof Player) {
						// select tag for compass
					} else {
						sender.sendMessage(Colors.paint(main.getMessages().getString("player-only")));
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					 MapTag tag = main.getGlobalList().stream().filter(marker -> "id".equals(marker.getId()))
					  .findAny()
					  .orElse(null);
					 if(tag != null) {
						 main.getGlobalList().remove(tag);
					 } else {
						 sender.sendMessage("Такой метки не существует!");
					 }
					// sender.sendMessage(Colors.paint(main.getMessages().getString("command.removed")));
				} 
				
				
				else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else if (args.length == 5) {
				if (args[0].equalsIgnoreCase("create")) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						boolean isLocal = false;
						if (args[4].equalsIgnoreCase("local"))
							isLocal = true;
						main.addTag(args[1], args[2], args[3], p, isLocal);
						// sender.sendMessage(Colors.paint(main.getMessages().getString("command.created")));

						return true;
					} else {
						sender.sendMessage(Colors.paint(main.getMessages().getString("player-only")));
					}
				} else {
					sender.sendMessage(Colors.paint(main.getConfig().getString("args")));
				}
			} else if(args.length == 3)  {   
				 if(args[0].equalsIgnoreCase("share")) {
					if(sender instanceof Player) {
						main.getMySQL().setPlayerPermission(Bukkit.getPlayer(args[1]).getUniqueId(),args[2]);
						
					}
				 } 
				
			
			} else if(args[0].equalsIgnoreCase("unshare")) {
				main.getMySQL().removePermission(Bukkit.getPlayer(args[1]).getUniqueId(), args[2]);
				
			}
			else {
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

	}

}
