package com.sciencebitch.tileentities;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public abstract class TileEntityElectricMachineBase extends TileEntity implements IInventory, ITickable, IEnergySink {

	private final String name;
	private String customName;

	private NonNullList<ItemStack> inventory;

	private int maxEnergyInput = 20;
	private final int energyCapacity;

	protected int energyStored;

	public TileEntityElectricMachineBase(String name, int energyCapacity) {

		this.name = "container." + name;
		this.energyCapacity = energyCapacity;

		this.inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	}

	public void setMaxEnergyInput(int maxEnergyInput) {
		this.maxEnergyInput = maxEnergyInput;
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
		return this.maxEnergyInput;
	}

	@Override
	public int getCapacityLeft(ItemStack stack) {
		return this.energyCapacity - this.energyStored;
	}

	@Override
	public int injectEnergy(IEnergyProvider provider, int amount, ItemStack stack) {

		amount = Math.min(amount, getCapacityLeft(stack));
		energyStored += amount;

		return amount;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEmpty() {

		for (ItemStack stack : inventory) {
			if (!stack.isEmpty())
				return false;
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

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
		inventory.clear();
	}

}
