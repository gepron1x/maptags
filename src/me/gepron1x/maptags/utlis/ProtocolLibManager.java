package me.gepron1x.maptags.utlis;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class ProtocolLibManager {
	private ProtocolManager protocolManager;
	public ProtocolLibManager() {
		protocolManager = ProtocolLibrary.getProtocolManager();
	}
	
}
