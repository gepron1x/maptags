package me.gepron1x.maptags.events;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


import me.gepron1x.maptags.utlis.MapTag;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
public class WaypointsListener implements Listener {
	private String actionbar;
	private String reached;
	private HashMap<UUID,MapTag> waypoints;
	public WaypointsListener() {
		this.waypoints = new HashMap<UUID,MapTag>();
		this.actionbar = "Расстояние до метки \"%maptag%\": %distance%"; //MapTagsPlugin.getInstance().getMessages().getString("waypoints.actionbar");
		this.reached = "Вы успешно дошли до метки.";//MapTagsPlugin.getInstance().getMessages().getString("waypoints.reached");
		
	}
	

@EventHandler
public void onMove(PlayerMoveEvent e) {
	Player p = e.getPlayer();
	 MapTag tag = waypoints.get(p.getUniqueId());
 if(tag == null) return;
 Integer distance = (int) p.getLocation().distance(tag.getLocation());
 
 TextComponent text = new TextComponent(actionbar.replace("%distance%", distance.toString()).replaceAll("%maptag%", tag.getName()));
 p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
if(distance == 0) {
	p.sendMessage(reached);
	waypoints.remove(p.getUniqueId());
     }

}
public void addWayPoint(Player p,MapTag tag) {
	waypoints.put(p.getUniqueId(), tag);
    }
public void removeWayPoint(Player p) {
	waypoints.remove(p.getUniqueId());
}


}

