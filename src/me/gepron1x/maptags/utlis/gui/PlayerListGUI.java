package me.gepron1x.maptags.utlis.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.MapTag;
import net.md_5.bungee.api.ChatColor;

public class PlayerListGUI implements MultiPagedInv,InventoryHolder {
	public enum Share {
		UNSHARE,
		SHARE;
	}
	private List<Inventory> pages;
	private MapTag tagtoshare;
	
	private int lastpage = 0;
	private int page = 0;
	private int lastslot = 0;
	private Inventory openedPage;
	Share type;
	private ItemStack nopage;
	private HashMap<ItemStack,Player> clickables = new HashMap<ItemStack,Player>();
	private String title;
	private ItemStack nextPage;
	private MapTagsPlugin plugin = MapTagsPlugin.getInstance();
	private ItemStack previousPage;
  public PlayerListGUI(String title,MapTag shareto,Share type,Player ignored) {
	  pages = new ArrayList<Inventory>();
	  this.type = type;
	 this.title = title;
	 this.tagtoshare = shareto;
	  updateSetup();
	  build(ignored);
     
  }
  private void build(Player ignored) {
	  int i = lastpage + 1;
		Integer pg = i;
		Inventory inv = Bukkit.createInventory(this, 6 * 9, title.replace("%page%", pg.toString()));
		pages.clear();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(p.equals(ignored)) continue;
			ItemStack ist = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) ist.getItemMeta();
			meta.setDisplayName(ChatColor.GRAY + p.getDisplayName());
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
			ist.setItemMeta(meta);
			inv.setItem(lastslot, ist);
			clickables.put(ist, p);
			lastslot++;
			if (lastslot == 45) {
				inv.setItem(46, previousPage);
				inv.setItem(52, nextPage);
				pages.add(inv);
				lastpage++;
				i = lastpage + 1;
				inv = Bukkit.createInventory(this, 6 * 9, title.replace("%page%", pg.toString()));
				lastslot = 0;
			}
		}
		inv.setItem(46, previousPage);
		inv.setItem(52, nextPage);
		pages.add(inv);
  }
	@Override
	public void next() {
	page++;
		
	}

	@Override
	public void previous() {
		page--;
		
	}

	@Override
	public void setPage(int page) {
		this.page = page;
		
	}

	@Override
	public int getLastPage() {
		return 0;
	}

	@Override
	public ItemStack getNext() {
		return this.nextPage;
	}

	@Override
	public ItemStack getPrevious() {
		return this.previousPage;
	}

	@Override
	public ItemStack getNoPage() {
		
		return this.nopage;
	}

	@Override
	public int getPage() {

		return page;
	}

	@Override
	public Inventory getInventory() {
		openedPage = pages.get(page);
		return openedPage;
	}
	public void updateSetup() {
		this.nopage = Colors.buildItemStackFromConfig("gui.list.nopage");
		this.nextPage = Colors.buildItemStackFromConfig("gui.list.nextPage");
		this.previousPage = Colors.buildItemStackFromConfig("gui.list.previousPage");
	}
	public void ClickHandler(InventoryClickEvent e) {
		e.setCancelled(true);
		Player p = clickables.get(e.getCurrentItem());
		if(p == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem() == null) return;
		switch(type) {
		case SHARE:
			plugin.getMySQL().setPlayerPermission(p.getUniqueId(), tagtoshare.getId());
			break;
		case UNSHARE:
			plugin.getMySQL().removePermission(p.getUniqueId(), tagtoshare.getId());
			break;
		}
		e.getWhoClicked().closeInventory();
		
	}

}
