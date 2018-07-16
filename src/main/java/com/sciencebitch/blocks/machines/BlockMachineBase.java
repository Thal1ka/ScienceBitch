package com.sciencebitch.blocks.machines;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.creativeTabs.SB_CreativeTabs;
import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.items.SB_Items;
import com.sciencebitch.mod.ScienceBitch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMachineBase extends Block implements IHasModel {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	protected final boolean isBurning;
	private final int guiId;

	public BlockMachineBase(String name, boolean isBurning, int guiId) {

		super(Material.IRON);

		setSoundType(SoundType.METAL);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.isBurning = isBurning;
		this.guiId = guiId;

		setUnlocalizedName(name);
		setRegistryName(name);
		SB_Blocks.BLOCKS.add(this);

		if (!isBurning) {
			this.setCreativeTab(SB_CreativeTabs.TAB_MACHINES);
			SB_Items.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		}
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

		if (!worldIn.isRemote) {
			IBlockState north = worldIn.getBlockState(pos.north());
			IBlockState south = worldIn.getBlockState(pos.south());
			IBlockState west = worldIn.getBlockState(pos.west());
			IBlockState east = worldIn.getBlockState(pos.east());
			EnumFacing face = state.getValue(FACING);

			if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock()) {
				face = EnumFacing.SOUTH;
			} else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock()) {
				face = EnumFacing.NORTH;
			} else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock()) {
				face = EnumFacing.EAST;
			} else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock()) {
				face = EnumFacing.WEST;
			}
			worldIn.setBlockState(pos, state.withProperty(FACING, face), 2);
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);
		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			playerIn.openGui(ScienceBitch.instance, guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
}
