package me.gepron1x.maptags.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.ASHologram;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.MapTag;
import me.gepron1x.maptags.utlis.WayPoint;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class WaypointsListener implements Listener {
	private String reached;
	private String notselected;
	private HashMap<UUID, WayPoint> waypoints;
	MapTagsPlugin main;

	public WaypointsListener() {
		waypoints = new HashMap<UUID, WayPoint>();
		main = MapTagsPlugin.getInstance();
		reloadMessages();

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		WayPoint point = waypoints.get(p.getUniqueId());

		
		if (point == null)
			return;
		
		point.onMove();
		
		if (point.getDistance() <= 1) {
			p.sendMessage(reached);
			removeWayPoint(e.getPlayer());
		}
	

	}

	public void addWayPoint(Player p, MapTag tag) {
		WayPoint point = new WayPoint(tag,p);
		waypoints.put(p.getUniqueId(), point);
		
		
		
		
	}

	public void removeWayPoint(Player p) {
		if (waypoints.get(p.getUniqueId()) == null) {
			p.sendMessage(notselected);
			return;
		}
		waypoints.get(p.getUniqueId()).destroy();
		waypoints.remove(p.getUniqueId());
		
		
	}

	public void reloadMessages() {
		this.notselected = Colors.paint(main.getMessages().getString("waypoints.notselected"));
		this.reached = Colors.paint(main.getMessages().getString("waypoints.reached"));
	}

}
