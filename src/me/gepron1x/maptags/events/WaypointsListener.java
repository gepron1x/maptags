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

		TextComponent text = new TextComponent(
				actionbar.replace("%distance%", distance.toString()).replaceAll("%maptag%", tag.getName()));
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
		holo.changeLocation();
		holo.changeName("Локация етпа");
		if (distance == 0) {
			p.sendMessage(reached);
			waypoints.remove(p.getUniqueId());
		}

	}

	public void addWayPoint(Player p, MapTag tag) {
		waypoints.put(p.getUniqueId(), tag);
		ASHologram hologram = new ASHologram(p);
		holos.put(p, hologram);
		hologram.spawn();
		hologram.changeName("Sex is dead");
		
		
	}

	public void removeWayPoint(Player p) {
		if (waypoints.get(p.getUniqueId()) == null) {
			p.sendMessage(notselected);
			return;
		}
		waypoints.remove(p.getUniqueId());
	}

	public void reloadMessages() {
		this.notselected = Colors.paint(main.getMessages().getString("waypoints.notselected"));
		this.actionbar = Colors.paint(main.getMessages().getString("waypoints.actionbar"));
		this.reached = Colors.paint(main.getMessages().getString("waypoints.reached"));
	}

}
