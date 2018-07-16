package com.sciencebitch.creativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTabBase extends CreativeTabs {

	private ItemStack iconStack = new ItemStack(Blocks.DIRT);

	public CreativeTabBase(String name) {

		super(name);
	}

	@Override
	public ItemStack getTabIconItem() {
		return iconStack;
	}

	public void setIcon(ItemStack iconStack) {
		this.iconStack = iconStack;
	}
}
