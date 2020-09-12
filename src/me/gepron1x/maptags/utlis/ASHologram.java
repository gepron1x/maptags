package me.gepron1x.maptags.utlis;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;


public class ASHologram {
	private ProtocolManager protocolManager;
	private int entityID;
	private Player handler;
	public ASHologram(Player p) {
		this.handler = p;
		protocolManager = ProtocolLibrary.getProtocolManager();
	}
	public void spawn() {
		 PacketContainer handle = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
	        handle.getModifier().writeDefaults();
          
	       entityID = (int)(Math.random() * Integer.MAX_VALUE);
	        handle.getIntegers().write(0, entityID);

	        handle.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
	        handle.getDoubles().write(0, handler.getLocation().getX()); // x
	        handle.getDoubles().write(1, handler.getLocation().getY()); // y
	        handle.getDoubles().write(2, handler.getLocation().getZ()); // z
	 	   PacketContainer handle2 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
	       handle2.getModifier().writeDefaults();
	       handle2.getIntegers().write(0, entityID);
	       WrappedDataWatcher dataWatcher = new WrappedDataWatcher(handle2.getWatchableCollectionModifier().read(0));
	       WrappedDataWatcher.WrappedDataWatcherObject isInvisibleIndex = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
	       dataWatcher.setObject(isInvisibleIndex, (byte) 0x20);
	        try {
	            ProtocolLibrary.getProtocolManager().sendServerPacket(handler, handle);
	        } catch (InvocationTargetException e) {
	            throw new RuntimeException("Cannot send packet.", e);
	        }

	        // custom name etc

	     
	}
  public void changeLocation() {
	  PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
      packet.getIntegers().write(0, this.entityID); //айди энтити
      packet.getDoubles().write(0, handler.getLocation().getX()+3); //x
      packet.getDoubles().write(1, handler.getLocation().getY()+1); //y
      packet.getDoubles().write(2, handler.getLocation().getZ()+3); //z
      packet.getBytes().write(0, (byte) 0);
      packet.getBytes().write(1, (byte) 0);
      packet.getBooleans().write(0, false);
      try {
		this.protocolManager.sendServerPacket(handler, packet);
	} catch (InvocationTargetException e) {
		// TODO Автоматически созданный блок catch
		e.printStackTrace();
	}
	  
  }
 public void changeName(String name) {
	   PacketContainer handle2 = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
       handle2.getIntegers().write(0, entityID);
       WrappedDataWatcher dataWatcher = new WrappedDataWatcher(handle2.getWatchableCollectionModifier().read(0));
       WrappedDataWatcher.WrappedDataWatcherObject nameValue = new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class));
       WrappedDataWatcher.WrappedDataWatcherObject nameVisible = new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class));
       dataWatcher.setObject(nameValue, name);
       dataWatcher.setObject(nameVisible, true);
       handle2.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
       try {
           ProtocolLibrary.getProtocolManager().sendServerPacket(handler, handle2);
       } catch (InvocationTargetException e) {
           throw new RuntimeException("Cannot send packet.", e);
       }
 }
}
