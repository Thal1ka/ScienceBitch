package com.sciencebitch.items;

import com.sciencebitch.creativeTabs.SB_CreativeTabs;
import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.util.CrossReferenceRegister;

import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name) {

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(SB_CreativeTabs.TAB_ITEMS);

		SB_Items.ITEMS.add(this);
		CrossReferenceRegister.registerItem(this, name);
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
