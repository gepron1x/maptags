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
	private boolean isHologramsEnabled,isActionBarEnabled,isDistanceHologramEnabled,isGlowEnabled;
	private MapTagsPlugin plugin;

	public WayPoint(MapTag target, Player p) {
		
		this.player = p;
		plugin = MapTagsPlugin.getInstance();
		this.target = target;
		isHologramsEnabled = plugin.getConfig().getBoolean("waypoints.holograms.enabled");
		isActionBarEnabled = plugin.getConfig().getBoolean("waypoints.actionbar.enabled");
		isDistanceHologramEnabled = plugin.getConfig().getBoolean("waypoints.holograms.distance.enabled");
		isGlowEnabled = plugin.getConfig().getBoolean("waypoints.holograms.podzkazka.enabled");
		if(isHologramsEnabled == false) {
			isDistanceHologramEnabled = false;
			isGlowEnabled = false;
		}
		this.actionbar = Colors.paint(plugin.getMessages().getString("waypoints.distance"));
		Location tloc = this.target.getLocation().clone();
		Location head = p.getLocation().add(0, p.getEyeHeight(), 0);
		Vector direction = tloc.subtract(head).toVector().normalize().multiply(5);
	
	 if(isDistanceHologramEnabled) {
		this.hologram = new ASHologram(p, target.getName(), EntityType.ARMOR_STAND, p.getLocation().add(direction), false);
		hologram.spawn();
	  }
	  if(isGlowEnabled) {
		  this.glow = new ASHologram(p, Colors.paint(plugin.getConfig().getString("waypoints.holograms.podzkazka.text")), EntityType.ARMOR_STAND, target.getLocation(), true);
			glow.spawn();
	}
	 
		
		
	}

	private String getActionBar() {
		Integer distance = (int) player.getLocation().distance(target.getLocation());
		String temp = actionbar.replace("%distance%", distance.toString()).replaceAll("%maptag%", target.getName());
        
		return temp;
	}

	public void onMove() {
		String msg = getActionBar();
		if(isActionBarEnabled)
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
		Location tloc = this.target.getLocation().clone();
		Location head = player.getLocation().add(0, player.getEyeHeight(), 0);
	  if(isDistanceHologramEnabled) {
		   hologram.setLocation(head, tloc);
			hologram.setName(msg);  
	   }
		

	}

	public void destroy() {
	  if(isGlowEnabled)
		glow.destroy();
	 if(isDistanceHologramEnabled)
		hologram.destroy();
	}

	public int getDistance() {
		return (int) player.getLocation().distance(target.getLocation());
	}


public void respawnHolos(Player p) {
	this.player = p;
	
	if(isDistanceHologramEnabled) {
		hologram.setHandler(p);
		hologram.spawn();
	}
	if(isGlowEnabled) {
		glow.setHandler(p);
		glow.spawn();
	}
}
}
