package com.sciencebitch.blocks.cables;

import com.sciencebitch.blocks.BlockBase;
import com.sciencebitch.blocks.transformers.Voltage;
import com.sciencebitch.creativeTabs.SB_CreativeTabs;
import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.tileentities.cables.TileEntityCable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
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

	private final boolean covered;
	private final Voltage voltage;

	public BlockCableBase(String name, boolean covered, Voltage voltage) {

		super(name, Material.CLOTH);
		this.covered = covered;
		this.voltage = voltage;

		setCreativeTab(SB_CreativeTabs.TAB_ELECTRIC_ITEMS);
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

		TileEntityCable tileEntity = new TileEntityCable();
		tileEntity.setVoltage(voltage);

		return tileEntity;
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
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {

		state = updateConnections(world, pos, state);
		return state;
	}

	private IBlockState updateConnections(IBlockAccess world, BlockPos pos, IBlockState state) {

		for (EnumFacing direction : EnumFacing.VALUES) {
			state = updateConnection(world, pos, direction, state);
		}

		return state;
	}

	private IBlockState updateConnection(IBlockAccess world, BlockPos pos, EnumFacing direction, IBlockState state) {

		BlockPos neighborPos = pos.offset(direction);
		TileEntity neighbor = world.getTileEntity(neighborPos);

		boolean canConnect = canConnectTo(neighbor);

		IProperty<Boolean> dirProperty = getPropertyFromDirection(direction);
		return state.withProperty(dirProperty, canConnect);
	}

	private boolean canConnectTo(TileEntity connectTo) {

		if (connectTo instanceof IEnergyConnector) return ((IEnergyConnector) connectTo).getVoltage() == this.voltage;
		return (connectTo instanceof IEnergyStorage);
	}

	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
