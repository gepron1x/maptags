package me.gepron1x.maptags.utlis;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface FakeEntity {

	
public void spawn();
public void setLocation(Location loc);
public void setName(String name);
public void setHandler(Player p);
public void destroy();
}