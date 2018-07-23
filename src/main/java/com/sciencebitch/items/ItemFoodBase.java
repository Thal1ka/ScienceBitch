package com.sciencebitch.items;

import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.util.CrossReferenceRegister;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class ItemFoodBase extends ItemFood implements IHasModel {

	public ItemFoodBase(String name, int amount, float saturation, boolean isWolfFood) {

		super(amount, saturation, isWolfFood);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.FOOD);

		SB_Items.ITEMS.add(this);
		CrossReferenceRegister.registerItem(this, name);
	}

	public ItemFoodBase(String name, int amount, boolean isWolfFood) {
		this(name, amount, 0.6f, isWolfFood);
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
