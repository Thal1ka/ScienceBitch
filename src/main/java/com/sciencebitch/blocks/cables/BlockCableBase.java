package com.sciencebitch.blocks.cables;

import com.sciencebitch.blocks.BlockBase;
import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.tileentities.cables.TileEntityCable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
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
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

		for (EnumFacing direction : EnumFacing.VALUES) {
			updateConnection(worldIn, pos, direction, state);
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

	private void updateConnection(World world, BlockPos pos, EnumFacing direction, IBlockState state) {

		TileEntity neighbor = world.getTileEntity(pos.offset(direction));

		boolean canConnect = (neighbor instanceof IEnergyConnector || neighbor instanceof IEnergyStorage);

		IProperty<Boolean> dirProperty = getPropertyFromDirection(direction);
		world.setBlockState(pos, state.withProperty(dirProperty, canConnect), 2);

		if (canConnect) {
			TileEntityCable tileentity = (TileEntityCable) world.getTileEntity(pos);

			if (neighbor instanceof IEnergyConnector) {
				tileentity.addConnection((IEnergyConnector) neighbor);
			} else {
				tileentity.addConnection((IEnergyStorage) neighbor);
			}
		}
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
		// TODO Auto-generated method stub
		return null;
	}
}
