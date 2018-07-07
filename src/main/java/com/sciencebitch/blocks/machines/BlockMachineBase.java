package com.sciencebitch.blocks.machines;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.items.SB_Items;
import com.sciencebitch.mod.ScienceBitch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;

public abstract class BlockMachineBase extends Block implements IHasModel {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	protected final boolean isBurning;

	public BlockMachineBase(String name, boolean isBurning) {

		super(Material.IRON);

		setSoundType(SoundType.METAL);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.isBurning = isBurning;

		setUnlocalizedName(name);
		setRegistryName(name);
		SB_Blocks.BLOCKS.add(this);

		if (!isBurning) {
			this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
			SB_Items.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		}
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
}
