package com.sciencebitch.blocks;

import java.util.Random;

import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockElectricFurnace extends BlockBase implements ITileEntityProvider {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool BURNING = PropertyBool.create("burning");

	public BlockElectricFurnace(String name) {

		super(name, Material.IRON);

		setDefaultState(this.blockState.getBaseState().withProperty(BlockElectricFurnace.FACING, EnumFacing.NORTH).withProperty(BlockElectricFurnace.BURNING, false));
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
			playerIn.openGui(ScienceBitch.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ()); // TODO Id
		}

		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

		if (worldIn.isRemote) {

			IBlockState north = worldIn.getBlockState(pos.north());
			IBlockState south = worldIn.getBlockState(pos.south());
			IBlockState west = worldIn.getBlockState(pos.west());
			IBlockState east = worldIn.getBlockState(pos.east());
			EnumFacing face = state.getValue(BlockElectricFurnace.FACING);

			if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
				face = EnumFacing.SOUTH;
			} else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
				face = EnumFacing.NORTH;
			} else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
				face = EnumFacing.EAST;
			} else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
				face = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(BlockElectricFurnace.FACING, face), 2);
		}
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState state = worldIn.getBlockState(pos);
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		worldIn.setBlockState(pos, SB_Blocks.ELECTRIC_FURNACE.getDefaultState().withProperty(BlockElectricFurnace.FACING, state.getValue(BlockElectricFurnace.FACING)).withProperty(BlockElectricFurnace.BURNING, active), 3);

		if (tileEntity != null) {
			tileEntity.validate();
			worldIn.setTileEntity(pos, tileEntity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityElectricFurnace();
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {

		return this.getDefaultState().withProperty(BlockElectricFurnace.FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		worldIn.setBlockState(pos, getDefaultState().withProperty(BlockElectricFurnace.FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		TileEntityElectricFurnace tileEntity = (TileEntityElectricFurnace) worldIn.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(worldIn, pos, tileEntity);
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {

		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {

		return state.withProperty(BlockElectricFurnace.FACING, rot.rotate(state.getValue(BlockElectricFurnace.FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

		return state.withRotation(mirrorIn.toRotation(state.getValue(BlockElectricFurnace.FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {

		return new BlockStateContainer(this, new IProperty[] { BlockElectricFurnace.BURNING, BlockElectricFurnace.FACING });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {

		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(BlockElectricFurnace.FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockElectricFurnace.FACING).getIndex();
	}
}
