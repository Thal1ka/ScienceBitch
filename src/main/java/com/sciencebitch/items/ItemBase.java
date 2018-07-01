package com.sciencebitch.items;

import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name) {

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MATERIALS);

		SB_Items.ITEMS.add(this);
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
