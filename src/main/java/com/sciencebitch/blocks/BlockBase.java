package com.sciencebitch.blocks;

import com.sciencebitch.items.SB_Items;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {

	public BlockBase(String name, Material material) {

		super(material);

		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

		SB_Blocks.BLOCKS.add(this);
		SB_Items.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

}
