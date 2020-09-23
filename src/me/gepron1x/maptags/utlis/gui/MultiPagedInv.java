package me.gepron1x.maptags.utlis.gui;

import org.bukkit.inventory.ItemStack;

public interface MultiPagedInv {
public void next();
public void previous();
public void setPage(int page);
public int getPage();
public int getLastPage();
public ItemStack getNext();
public ItemStack getPrevious();
public ItemStack getNoPage();


}
