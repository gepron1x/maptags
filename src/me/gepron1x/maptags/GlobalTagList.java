package me.gepron1x.maptags;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class GlobalTagList {

	@SuppressWarnings("unused")
	private List<MapTag> maptags = new ArrayList<MapTag>();
	private MapTagsPlugin main = MapTagsPlugin.getInstance();

	public void addTag(String id, String name, String lore, Player p) {
		maptags.add(new MapTag(id, name, lore, p.getUniqueId(), p.getLocation(), p.getInventory().getItemInMainHand()));

	}

	public List<MapTag> getList() {
		return maptags;
	}

}
