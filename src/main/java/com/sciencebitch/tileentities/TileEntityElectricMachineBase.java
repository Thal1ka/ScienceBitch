package com.sciencebitch.tileentities;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityElectricMachineBase extends TileEntity implements IInventory, ITickable, IEnergySink {

	private final String name;
	private String customName;

	public TileEntityElectricMachineBase(String name) {

		this.name = "container." + name;
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : name;
	}

	@Override
	public boolean hasCustomName() {
		return customName != null;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public int getMaxEnergyInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCapacityLeft(ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int injectEnergy(IEnergyProvider provider, int amount, ItemStack stack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeInventory(EntityPlayer player) {
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
	public void clear() {
		// TODO Auto-generated method stub

	}

}
