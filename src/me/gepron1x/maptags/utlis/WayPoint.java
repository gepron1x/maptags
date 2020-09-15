package me.gepron1x.maptags.utlis;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.gepron1x.maptags.MapTagsPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WayPoint {
	private MapTag target;
	private ASHologram hologram;
	private ASHologram glow;
	private Player player;
	private String actionbar;
	private MapTagsPlugin plugin;

	public WayPoint(MapTag target, Player p) {
		this.player = p;
		plugin = MapTagsPlugin.getInstance();
		this.target = target;
		this.actionbar = plugin.getMessages().getString("waypoints.actionbar");
		Location tloc = this.target.getLocation();
		Location head = p.getLocation().add(0, p.getEyeHeight(), 0);
		Vector direction = tloc.subtract(head).toVector().normalize().multiply(3);
		this.hologram = new ASHologram(p, target.getName(), EntityType.ARMOR_STAND, p.getLocation().add(direction), false);
		this.glow = new ASHologram(p, Colors.paint("&cИди сюда!"), EntityType.ARMOR_STAND, target.getLocation(), true);
		glow.spawn();
		hologram.spawn();
	}

	private String getActionBar() {
		Integer distance = (int) player.getLocation().distance(target.getLocation());
		String temp = actionbar.replace("%distance%", distance.toString()).replaceAll("%maptag%", target.getName());
     
		return temp;
	}

	public void onMove(Player p) {
		String msg = getActionBar();
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
		Location tloc = this.target.getLocation();
		Location head = player.getLocation().add(0, player.getEyeHeight(), 0);
		hologram.setLocation(head, tloc);
		hologram.setName(msg);

	}

	public void destroy() {
		glow.destroy();
		hologram.destroy();
	}

	public int getDistance() {
		return (int) player.getLocation().distance(target.getLocation());
	}

}
