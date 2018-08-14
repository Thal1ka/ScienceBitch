package com.sciencebitch.blocks.generators;

import java.util.Random;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.blocks.machines.BlockMachineBase;
import com.sciencebitch.gui.SB_GUIs;
import com.sciencebitch.tileentities.generators.TileEntityLavaGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLavaGenerator extends BlockMachineBase {

	private static boolean keepInventory;

	public BlockLavaGenerator(String name, boolean isWorking) {
		super(name, isWorking, SB_GUIs.ID_LAVA_GENERATOR);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(SB_Blocks.GENERATOR_LAVA);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(SB_Blocks.GENERATOR_LAVA);
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, SB_Blocks.GENERATOR_LAVA_ACTIVE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		} else {
			worldIn.setBlockState(pos, SB_Blocks.GENERATOR_LAVA.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLavaGenerator();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if (!keepInventory) {
			TileEntityLavaGenerator tileentity = (TileEntityLavaGenerator) worldIn.getTileEntity(pos);

			InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

}
