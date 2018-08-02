package com.sciencebitch.tileentities.generators;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;
import com.sciencebitch.tileentities.TileEntityMachineBase;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public abstract class TileEntityGeneratorBase extends TileEntityMachineBase implements ITickable, IEnergyProvider {

	private int maxEnergyOutput = 100;
	private final int energyCapacity;

	protected int storedEnergy;

	public TileEntityGeneratorBase(String name, int energyCapacity) {

		super(name);
		this.energyCapacity = energyCapacity;
	}

	public void setMaxEnergyOutput(int maxEnergyOutput) {
		this.maxEnergyOutput = maxEnergyOutput;
	}

	@Override
	public int getMaxEnergyOutput() {
		return maxEnergyOutput;
	}

	@Override
	public int getEnergyLeft(ItemStack stack) {
		return storedEnergy;
	}

	@Override
	public int drainEnergy(EntityLivingBase entity, int amount, ItemStack stack) {

		amount = Math.min(amount, getEnergyLeft(stack));
		storedEnergy -= amount;

		return amount;
	}

	@Override
	public void update() {

		boolean isWorkingBeforeUpdate = isWorking();
		boolean canWork = canWork();

		if (hasEnergy()) {
			handleEnergy();
		}

		if (world.isRemote) return;

		if (canWork) {
			doWork();
		}

		boolean isWorkingAfterUpdate = isWorking();

		if (isWorkingBeforeUpdate != isWorkingAfterUpdate) {
			updateState(isWorkingAfterUpdate, this.world, this.pos);
			this.markDirty();
		}
	}

	protected boolean hasEnergy() {
		return storedEnergy > 0;
	}

	private void handleEnergy() {

		ItemStack chargeStack = getChargeStack();
		if (chargeStack.isEmpty()) return;

		EnergyHelper.transferEnergy(this, (IEnergySink) chargeStack.getItem(), chargeStack);
	}

	protected int getCapacityLeft() {
		return energyCapacity - storedEnergy;
	}

	protected void generateEnergy(int amount) {

		storedEnergy = Math.min(storedEnergy + amount, energyCapacity);
	}

	public static boolean isItemChargable(ItemStack stack) {
		return (stack.getItem() instanceof IEnergySink);
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

	protected boolean isItemFullyCharged() {

		ItemStack chargeStack = getChargeStack();
		if (chargeStack.isEmpty()) return true;

		IEnergySink sink = (IEnergySink) chargeStack.getItem();
		return sink.getCapacityLeft(chargeStack) <= 0;
	}

	protected abstract ItemStack getChargeStack();

	protected abstract boolean isWorking();

	protected abstract boolean canWork();

	protected abstract void doWork();
}
