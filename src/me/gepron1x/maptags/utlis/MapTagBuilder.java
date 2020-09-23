package me.gepron1x.maptags.utlis;



import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


import me.gepron1x.maptags.MapTagsPlugin;
import net.wesjd.anvilgui.AnvilGUI;


public class MapTagBuilder implements InventoryHolder {
    private static int id = 0;
    private boolean isLocal = false;
    private String name;
    private String lore;
    private Player player;
    private Inventory typeselection;
    private ItemStack global;
    private ItemStack local;
    private MapTagsPlugin plugin = MapTagsPlugin.getInstance();
public MapTagBuilder(Player p) {
  typeselection = Bukkit.createInventory(this, 3*9, "Выберите тип");
  global = new ItemStack(Material.ANDESITE_SLAB);//Colors.buildItemStackFromConfig("gui.creation.type.global");
  local = new ItemStack(Material.CUT_RED_SANDSTONE);//Colors.buildItemStackFromConfig("gui.creation.type.local");
  typeselection.setItem(11, global);
	typeselection.setItem(15, local);
	buildName();
}
public void buildName() {
	new AnvilGUI.Builder()
    .onClose(player -> {                      //called when the inventory is closing
        player.sendMessage("You closed the inventory.");
    })
    .onComplete((player, text) -> {           //called when the inventory output slot is clicked
      name = text;
      buildLore();
      return AnvilGUI.Response.close();
    })
    .preventClose()                           //prevents the inventory from being closed
    .text("Введи название сюда")     //sets the text the GUI should start with
    .item(new ItemStack(Material.PAPER)) //use a custom item for the first slot
    .title("Введи название сюда")              //set the title of the GUI (only works in 1.14+)
    .plugin(plugin)                 //set the plugin instance
    .open(player);
}
public void buildLore() {
	new AnvilGUI.Builder()
    .onClose(player -> {                      //called when the inventory is closing
        player.sendMessage("You closed the inventory.");
    })
    .onComplete((player, text) -> {           //called when the inventory output slot is clicked
      lore = text;
      player.openInventory(getInventory());
      return AnvilGUI.Response.close();
    })
    .preventClose()                           //prevents the inventory from being closed
    .text("Введи описание сюда")     //sets the text the GUI should start with
    .item(new ItemStack(Material.PAPER)) //use a custom item for the first slot
    .title("Введи описание сюда")              //set the title of the GUI (only works in 1.14+)
    .plugin(plugin)                 //set the plugin instance
    .open(player);
}

public void buildTag() {
	Integer idd = id;
	plugin.addTag(idd.toString(), name, lore, player, isLocal);
}
public void setIsLocal(InventoryClickEvent e) {
	
	e.setCancelled(true);
    if(e.getCurrentItem().equals(global)) {
    	this.isLocal = false;

	} else if(e.getCurrentItem().equals(local)) {
		this.isLocal = true;
	
	} else {
		return;
	}
	e.getWhoClicked().closeInventory();
	buildTag();
}
@Override
public Inventory getInventory() {
	// TODO Автоматически созданная заглушка метода
	return typeselection;
	
}
}



