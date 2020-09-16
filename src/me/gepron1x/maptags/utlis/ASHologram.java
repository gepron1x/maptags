package me.gepron1x.maptags.utlis;



import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerEntityTeleport;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.protocol.utility.Util;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.ChatColor;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

public class ASHologram {
	private int entityID;
	private WrapperPlayServerSpawnEntity spawn;
	private WrapperPlayServerEntityMetadata meta;
	private WrapperPlayServerEntityDestroy destroy;
	private String name;
	private Player handler;

	public ASHologram(Player p, String name, EntityType type,Location loc,boolean isGlowing) {
		UUID uuid = UUID.randomUUID();
	   
		this.name = name;
		byte meta;
		if(isGlowing == true) {
			meta = 0x20 | 0x40;
		} else {
			meta = 0x20;
		}
		this.entityID = (int) (Math.random() * Integer.MAX_VALUE);
		this.handler = p;
		this.spawn = new WrapperPlayServerSpawnEntity();
		this.meta = new WrapperPlayServerEntityMetadata();
		this.destroy = new WrapperPlayServerEntityDestroy();
		this.spawn.setType(type);
		this.spawn.setEntityID(entityID);
		this.spawn.setUniqueId(uuid);
		this.spawn.setX(loc.getX());
		this.spawn.setY(loc.getY());
		this.spawn.setZ(loc.getZ());
		WrappedChatComponent nick = WrappedChatComponent.fromText(name);
		List<WrappedWatchableObject> obj = Util.asList(
				new WrappedWatchableObject(new WrappedDataWatcherObject(14, Registry.get(Byte.class)), (byte) 0x01),
				new WrappedWatchableObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)), meta),
				new WrappedWatchableObject(new WrappedDataWatcherObject(3, Registry.get(Boolean.class)), true),
				new WrappedWatchableObject(new WrappedDataWatcherObject(2, Registry.getChatComponentSerializer(true)),
						Optional.of(nick.getHandle())));
		this.meta = new WrapperPlayServerEntityMetadata();
		this.meta.setEntityID(entityID);
		this.meta.setMetadata(obj);
		this.destroy.setEntityIds(new int[] { entityID });
		spawn();
		
	}

	public void spawn() {
		this.spawn.sendPacket(handler);
		this.meta.sendPacket(handler);

	}

	public void setLocation(Location loc, Location target) {
		WrapperPlayServerEntityTeleport teleport = new WrapperPlayServerEntityTeleport();
		teleport.setEntityID(entityID);
		//
		Vector direction = target.subtract(loc).toVector().normalize().multiply(3);
		//Location l = loc.add(direction);
		Location l = loc.add(direction.getX(), 0, direction.getZ());
		teleport.setX(l.getX());
		teleport.setY(l.getY());
		teleport.setZ(l.getZ());
		//
		teleport.sendPacket(handler);

	}

	public void setName(String name) {
		this.name = name;
		WrappedChatComponent nick = WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', name));
		this.name = name;
		this.meta.getMetadata().get(3).setValue(Optional.of(nick.getHandle()));
		meta.sendPacket(handler);
	}
	public void destroy() {
		this.destroy.sendPacket(handler);
	}
	public void setHandler(Player p) {
		this.handler = p;
	}
	public String getName() {
		return name;
	}
}
