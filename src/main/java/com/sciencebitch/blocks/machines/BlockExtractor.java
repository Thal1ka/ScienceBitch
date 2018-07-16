package com.sciencebitch.blocks.machines;

import java.util.Random;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.TileEntityExtractor;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockExtractor extends BlockMachineBase implements ITileEntityProvider {

	private static boolean keepInventory = false;

	public BlockExtractor(String name, boolean isBurning) {

		super(name, isBurning);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(SB_Blocks.ELECTRIC_FURNACE);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(SB_Blocks.ELECTRIC_FURNACE);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			playerIn.openGui(ScienceBitch.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			worldIn.setBlockState(pos, SB_Blocks.ELECTRIC_FURNACE_ACTIVE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		} else {
			worldIn.setBlockState(pos, SB_Blocks.ELECTRIC_FURNACE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityExtractor();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityExtractor) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityExtractor) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

}