package com.sciencebitch.blocks.machines;

import java.util.Random;

import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.interfaces.ITileEntityCreator;
import com.sciencebitch.items.SB_Items;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.machines.TileEntityElectricFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMachineBaseExp extends Block implements IHasModel, ITileEntityProvider {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static String machineName;

	private static boolean keepInventory;
	private final ITileEntityCreator tileEntityCreator;
	private final boolean isBurning;
	private final int guiId;

	public BlockMachineBaseExp(String name, boolean isBurning, ITileEntityCreator tileEntityCreator, int guiId) {

		super(Material.IRON);
		setSoundType(SoundType.METAL);

		this.machineName = name;

		this.tileEntityCreator = tileEntityCreator;
		this.isBurning = isBurning;
		this.guiId = guiId;
		setUnlocalizedName(name);
		setRegistryName(name);

		if (!isBurning) {
			this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
			SB_Items.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		}
	}

	@Override
	public void registerModel() {

		ScienceBitch.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");

	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		Block idleBlock = MachineCreator.instance().getMachine(getUnlocalizedName()).getIdleBlock();
		return Item.getItemFromBlock(idleBlock);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {

		Block idleBlock = MachineCreator.instance().getMachine(getUnlocalizedName()).getIdleBlock();
		return new ItemStack(idleBlock);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			playerIn.openGui(ScienceBitch.instance, this.guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
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

	public static void setState(boolean active, World worldIn, BlockPos pos) {

		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		keepInventory = true;

		if (active) {
			Block activeBlock = MachineCreator.instance().getMachine(machineName).getActiveBlock();
			worldIn.setBlockState(pos, activeBlock.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		} else {
			Block idleBlock = MachineCreator.instance().getMachine(machineName).getIdleBlock();
			worldIn.setBlockState(pos, idleBlock.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		}

		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return this.tileEntityCreator.createNewTileEntity(worldIn, meta);
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityElectricFurnace) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
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

}
