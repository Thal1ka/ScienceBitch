package com.sciencebitch.blocks.machines;

import java.util.Random;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.gui.SB_GUIs;
import com.sciencebitch.tileentities.machines.TileEntityPulverizer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPulverizer extends BlockMachineBase {

	private static boolean keepInventory = false;

	public BlockPulverizer(String name, boolean isBurning) {

		super(name, isBurning, SB_GUIs.ID_PULVERIZER);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return Item.getItemFromBlock(SB_Blocks.PULVERIZER);

	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(SB_Blocks.PULVERIZER);
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, SB_Blocks.PULVERIZER_ACTIVE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		} else {
			worldIn.setBlockState(pos, SB_Blocks.PULVERIZER.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPulverizer();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityPulverizer) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityPulverizer) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
