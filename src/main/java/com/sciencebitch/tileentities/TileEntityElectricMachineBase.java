package com.sciencebitch.tileentities;

import com.sciencebitch.interfaces.energy.IEnergyProvider;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEntityElectricMachineBase extends TileEntityMachineBase implements ITickable, IEnergyStorage {

	private boolean burningBeforeUpdate;

	private int maxEnergyInput = 20;
	private final int energyCapacity;

	protected int storedEnergy;

	public TileEntityElectricMachineBase(String name, int energyCapacity) {

		super(name);
		this.energyCapacity = energyCapacity;
	}

	public void setMaxEnergyInput(int maxEnergyInput) {
		this.maxEnergyInput = maxEnergyInput;
	}

	@Override
	public void update() {

		handleEnergy();

		if (world.isRemote) return;

		boolean isWorking = canWork() && hasEnergy();

		if (isWorking) {

			doWork();
			storedEnergy--;
		} else {

			onWorkCanceled();
		}

		if (burningBeforeUpdate != isWorking) {

			burningBeforeUpdate = isWorking;
			updateState(isWorking, this.world, this.pos);
			this.markDirty();
		}
	}

	protected boolean hasEnergy() {
		return storedEnergy > 0;
	}

	private void handleEnergy() {

		ItemStack fuelStack = getFuelStack();
		if (fuelStack.isEmpty()) return;

		EnergyHelper.transferEnergy((IEnergyProvider) fuelStack.getItem(), fuelStack, this);
	}

	public static boolean isItemFuel(ItemStack stack) {
		return (stack.getItem() instanceof IEnergyProvider);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);

		burningBeforeUpdate = compound.getBoolean("burningBeforeUpdate");
		storedEnergy = compound.getInteger("storedEnergy");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		compound.setBoolean("burningBeforeUpdate", burningBeforeUpdate);
		compound.setInteger("storedEnergy", storedEnergy);

		return compound;
	}

	protected boolean isFuelEmpty() {

		ItemStack fuelStack = getFuelStack();
		if (fuelStack.isEmpty()) return true;

		IEnergyProvider provider = (IEnergyProvider) fuelStack.getItem();

		return provider.getEnergyLeft(fuelStack) <= 0;
	}

	protected int getCapacityLeft() {
		return energyCapacity - storedEnergy;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {

		int receiveAmount = Math.min(maxReceive, getCapacityLeft());
		receiveAmount = Math.min(receiveAmount, maxEnergyInput);

		if (!simulate) {
			storedEnergy += receiveAmount;
		}

		return receiveAmount;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return this.storedEnergy;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.energyCapacity;
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	protected abstract ItemStack getFuelStack();

	protected abstract boolean canWork();

	protected abstract void doWork();

	protected abstract void onWorkCanceled();
}
