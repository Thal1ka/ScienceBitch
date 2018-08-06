package com.sciencebitch.blocks.cables;

import java.util.Random;

import com.sciencebitch.blocks.BlockBase;
import com.sciencebitch.creativeTabs.SB_CreativeTabs;
import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.tileentities.cables.TileEntityCable;
import com.sciencebitch.util.EnergyStoragePosition;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockCableBase extends BlockBase implements ITileEntityProvider {

	public static final IProperty<Boolean> DOWN = PropertyBool.create("down");
	public static final IProperty<Boolean> UP = PropertyBool.create("up");
	public static final IProperty<Boolean> NORTH = PropertyBool.create("north");
	public static final IProperty<Boolean> SOUTH = PropertyBool.create("south");
	public static final IProperty<Boolean> WEST = PropertyBool.create("west");
	public static final IProperty<Boolean> EAST = PropertyBool.create("east");

	public BlockCableBase(String name) {
		super(name, Material.CLOTH);

		setCreativeTab(SB_CreativeTabs.TAB_ELECTRIC_ITEMS);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		for (EnumFacing facing : EnumFacing.VALUES) {
			state = updateConnection(worldIn, pos, facing, state);
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {

		IBlockState state = world.getBlockState(pos);
		World worldIn = world.getTileEntity(pos).getWorld();

		int dx = pos.getX() - neighbor.getX();
		int dy = pos.getY() - neighbor.getY();
		int dz = pos.getZ() - neighbor.getZ();

		if (dx < 0) {
			updateConnection(worldIn, pos, EnumFacing.EAST, state);
		} else if (dx > 0) {
			updateConnection(worldIn, pos, EnumFacing.WEST, state);
		} else if (dy < 0) {
			updateConnection(worldIn, pos, EnumFacing.UP, state);
		} else if (dy > 0) {
			updateConnection(worldIn, pos, EnumFacing.DOWN, state);
		} else if (dz < 0) {
			updateConnection(worldIn, pos, EnumFacing.SOUTH, state);
		} else if (dz > 0) {
			updateConnection(worldIn, pos, EnumFacing.NORTH, state);
		}
	}

	private IBlockState updateConnection(World world, BlockPos pos, EnumFacing direction, IBlockState state) {

		BlockPos neighborPos = pos.offset(direction);
		TileEntity neighbor = world.getTileEntity(neighborPos);

		boolean canConnect = (neighbor instanceof IEnergyConnector || neighbor instanceof IEnergyStorage);

		IProperty<Boolean> dirProperty = getPropertyFromDirection(direction);
		state = state.withProperty(dirProperty, canConnect);
		world.setBlockState(pos, state, 2);

		if (canConnect) {
			TileEntityCable tileentity = (TileEntityCable) world.getTileEntity(pos);

			if (neighbor instanceof IEnergyConnector) {
				System.out.println("Connect cable: " + neighborPos);
				tileentity.addConnection((IEnergyConnector) neighbor);
			} else {
				System.out.println("Connect Storage: " + neighborPos);
				tileentity.addConnection(new EnergyStoragePosition((IEnergyStorage) neighbor, neighborPos));
			}
		}

		return state;
	}

	private IProperty<Boolean> getPropertyFromDirection(EnumFacing direction) {

		switch (direction) {
			case DOWN:
				return DOWN;
			case UP:
				return UP;
			case NORTH:
				return NORTH;
			case WEST:
				return WEST;
			case SOUTH:
				return SOUTH;
			case EAST:
				return EAST;
			default:
				return NORTH;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCable();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { UP, DOWN, NORTH, WEST, SOUTH, EAST });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

	}
}
