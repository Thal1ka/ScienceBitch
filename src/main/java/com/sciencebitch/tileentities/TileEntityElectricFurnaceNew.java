package com.sciencebitch.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityElectricFurnaceNew extends TileEntityElectricMachineBase {

	public static final String NAME = "electric_furnace";
	public static final int CAPACITY = 200;

	public TileEntityElectricFurnaceNew() {
		super(NAME, CAPACITY);
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean canSmelt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected ItemStack getFuelStack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doWork() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onWorkCanceled() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {
		// TODO Auto-generated method stub

	}

}
