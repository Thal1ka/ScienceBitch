package com.sciencebitch.tileentities;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public abstract class TileEntityElectricMachineBase extends TileEntityMachineBase implements ITickable, IEnergySink {

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
	public int getMaxEnergyInput() {
		return this.maxEnergyInput;
	}

	@Override
	public int getCapacityLeft(ItemStack stack) {
		return this.energyCapacity - this.storedEnergy;
	}

	@Override
	public int injectEnergy(IEnergyProvider provider, int amount, ItemStack stack) {

		amount = Math.min(amount, getCapacityLeft(stack));
		storedEnergy += amount;

		return amount;
	}

	@Override
	public void update() {

		boolean isWorkingBeforeUpdate = hasEnergy();

		boolean canWork = canWork();

		if (hasEnergy()) {
			this.storedEnergy--;
		}

		if (canWork) {
			handleEnergy();
		}

		if (world.isRemote) return;

		if (canWork && hasEnergy()) {
			doWork();
		}

		if (!canWork || !hasEnergy()) {
			onWorkCanceled();
		}

		boolean isWorkingAfterUpdate = hasEnergy();

		if (isWorkingBeforeUpdate != isWorkingAfterUpdate) {
			updateState(isWorkingAfterUpdate, this.world, this.pos);
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

		storedEnergy = compound.getInteger("storedEnergy");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		compound.setInteger("storedEnergy", storedEnergy);

		return compound;
	}

	protected boolean isFuelEmpty() {

		ItemStack fuelStack = getFuelStack();
		if (fuelStack.isEmpty()) return true;

		IEnergyProvider provider = (IEnergyProvider) fuelStack.getItem();

		return provider.getEnergyLeft(fuelStack) <= 0;
	}

	protected abstract ItemStack getFuelStack();

	protected abstract boolean canWork();

	protected abstract void doWork();

	protected abstract void onWorkCanceled();
}
