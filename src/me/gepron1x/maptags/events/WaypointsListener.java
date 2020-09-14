package me.gepron1x.maptags.events;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.ASHologram;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.MapTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WaypointsListener implements Listener {
	private String actionbar;
	private String reached;
	private String notselected;
	private HashMap<UUID, MapTag> waypoints;
	private HashMap<Player,ASHologram> holos;
	MapTagsPlugin main;

	public WaypointsListener() {
		holos = new HashMap<Player,ASHologram>();
		waypoints = new HashMap<UUID, MapTag>();
		main = MapTagsPlugin.getInstance();
		reloadMessages();

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		MapTag tag = waypoints.get(p.getUniqueId());
		ASHologram holo = holos.get(p);
		
		if (tag == null || holo == null)
			return;
		Integer distance = (int) p.getLocation().distance(tag.getLocation());
         String temp = actionbar.replace("%distance%", distance.toString()).replaceAll("%maptag%", tag.getName());
		TextComponent text = new TextComponent(temp);
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
		holo.setLocation();
		holo.setName(temp);
		if (distance == 0) {
			p.sendMessage(reached);
			removeWayPoint(e.getPlayer());
		}
	

	}

	public void addWayPoint(Player p, MapTag tag) {
		waypoints.put(p.getUniqueId(), tag);
		ASHologram hologram = new ASHologram(p,tag.getName());
		holos.put(p, hologram);
		hologram.spawn(p);
		
		
		
	}

	public void removeWayPoint(Player p) {
		if (waypoints.get(p.getUniqueId()) == null || holos.get(p) == null) {
			p.sendMessage(notselected);
			return;
		}
		waypoints.remove(p.getUniqueId());
		holos.get(p).destroy();
		holos.remove(p);
		
	}

	public void reloadMessages() {
		this.notselected = Colors.paint(main.getMessages().getString("waypoints.notselected"));
		this.actionbar = Colors.paint(main.getMessages().getString("waypoints.actionbar"));
		this.reached = Colors.paint(main.getMessages().getString("waypoints.reached"));
	}

}
