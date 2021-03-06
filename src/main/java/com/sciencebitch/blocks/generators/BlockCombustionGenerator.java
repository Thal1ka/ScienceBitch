package com.sciencebitch.blocks.generators;

import java.util.Random;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.blocks.machines.BlockMachineBase;
import com.sciencebitch.gui.SB_GUIs;
import com.sciencebitch.tileentities.generators.TileEntityCombustionGenerator;
import com.sciencebitch.util.BlockHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCombustionGenerator extends BlockMachineBase {

	private static boolean keepInventory;

	public BlockCombustionGenerator(String name, boolean isBurning) {

		super(name, isBurning, SB_GUIs.ID_COMBUSTION_GENERATOR);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(SB_Blocks.GENERATOR_COMBUSTION);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(SB_Blocks.GENERATOR_COMBUSTION);
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, SB_Blocks.GENERATOR_COMBUSTION_ACTIVE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		} else {
			worldIn.setBlockState(pos, SB_Blocks.GENERATOR_COMBUSTION.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCombustionGenerator();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if (!keepInventory) {
			TileEntityCombustionGenerator tileentity = (TileEntityCombustionGenerator) worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCombustionGenerator) {
				BlockHelper.dropSpecialInventory(tileentity, worldIn, pos, new int[] { tileentity.ID_CURRENT_ITEMFIELD });
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
