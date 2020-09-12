package me.NukerFall.WG;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class IWorldGuard {

	RegionContainer rc = WorldGuard.getInstance().getPlatform().getRegionContainer();
	WorldGuardPlugin wg = WorldGuardPlugin.inst();
	RegionManager manager;

	public boolean isInRegion(Player p) {
		LocalPlayer pl = wg.wrapPlayer(p);
		manager = rc.get(pl.getWorld());
		ApplicableRegionSet regions = manager.getApplicableRegions(pl.getLocation().toVector().toBlockPoint());
		if (regions.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<String> getRegions(Player p) {
		LocalPlayer pl = wg.wrapPlayer(p);
		manager = rc.get(pl.getWorld());
		ApplicableRegionSet regions = manager.getApplicableRegions(pl.getLocation().toVector().toBlockPoint());
		if (regions.size() == 0) {
			List<String> names = new ArrayList<String>();
			for (ProtectedRegion reg : regions.getRegions()) {
				names.add(reg.getId());
			}
			return names;
		} else {
			return null;
		}
	}

	public boolean isInHisRegion(Player p) {
		LocalPlayer pl = wg.wrapPlayer(p);
		manager = rc.get(pl.getWorld());
		ApplicableRegionSet regions = manager.getApplicableRegions(pl.getLocation().toVector().toBlockPoint());
		for (ProtectedRegion r : regions) {
			if (!r.getOwners().contains(pl)) {
				if (!r.getMembers().contains(pl)) {
					return false;
				}
			}
		}
		return true;
	}

}
