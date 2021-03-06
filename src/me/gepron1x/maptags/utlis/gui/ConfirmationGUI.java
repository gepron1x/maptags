package me.gepron1x.maptags.utlis.gui;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.gepron1x.maptags.MapTagsPlugin;
import me.gepron1x.maptags.utlis.Colors;
import me.gepron1x.maptags.utlis.MapTag;
public class ConfirmationGUI implements InventoryHolder {
private Inventory inv;
private ItemStack yes;
private ItemStack no;
private MapTag tagToDelete;
private MapTagsPlugin main;
public ConfirmationGUI(MapTag tag) {
	main = MapTagsPlugin.getInstance();
	tagToDelete = tag;
	this.yes = Colors.buildItemStackFromConfig("gui.confirmation.yeah");
	this.no = Colors.buildItemStackFromConfig("gui.confirmation.nope");
	inv = Bukkit.createInventory(this, 9 *3,Colors.paint(main.getConfig().getString("gui.confirmation.title")));
	inv.setItem(11, yes);
	inv.setItem(15, no);
	
}
	@Override
	public Inventory getInventory() {
		// TODO ������������� ��������� �������� ������
		return inv;
	}
	public ItemStack getYes() {
		return yes;
	}
	public ItemStack getNo() {
		return no;
	}
public void confirmed() {
	main.removeTag(tagToDelete);
}
public void ClickHandler(InventoryClickEvent e) {

	e.setCancelled(true);
	if(e.getCurrentItem().equals(getYes())) {
		confirmed();
		e.getWhoClicked().closeInventory();
	} else if(e.getCurrentItem().equals(getNo())) {
		  e.getWhoClicked().closeInventory();
	} else {
		return;
	}
}

}
