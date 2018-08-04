package com.sciencebitch.tileentities.generators;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyReceiver;
import com.sciencebitch.tileentities.TileEntityMachineBase;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEntityGeneratorBase extends TileEntityMachineBase implements ITickable, IEnergyStorage {

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

		if (this.storedEnergy <= 0) return;

		ItemStack chargeStack = getChargeStack();
		if (!chargeStack.isEmpty()) {
			EnergyHelper.transferEnergy(this, (IEnergyReceiver) chargeStack.getItem(), chargeStack);
		}

		transferEnergyToNeighborBlocks();
	}

	private void transferEnergyToNeighborBlocks() {

		BlockPos[] neighbors = new BlockPos[] { pos.north(), pos.west(), pos.south(), pos.east(), pos.up(), pos.down() };

		List<IEnergyStorage> energyReceivers = new ArrayList<>();

		for (BlockPos neighbor : neighbors) {

			TileEntity tileEntity = world.getTileEntity(neighbor);

			if (tileEntity != null && tileEntity instanceof IEnergyStorage) {
				IEnergyStorage storage = (IEnergyStorage) tileEntity;

				if (storage.canReceive()) {
					energyReceivers.add(storage);
				}
			}
		}

		EnergyHelper.transferEnergyIntoBlocks(this, energyReceivers);
	}

	protected int getCapacityLeft() {
		return energyCapacity - storedEnergy;
	}

	protected void generateEnergy(int amount) {

		storedEnergy = Math.min(storedEnergy + amount, energyCapacity);
	}

	public static boolean isItemChargable(ItemStack stack) {
		return (stack.getItem() instanceof IEnergyReceiver);
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

		IEnergyReceiver sink = (IEnergyReceiver) chargeStack.getItem();
		return sink.getCapacityLeft(chargeStack) <= 0;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {

		int extractAmount = Math.min(maxExtract, storedEnergy);
		extractAmount = Math.min(extractAmount, maxEnergyOutput);

		if (!simulate) {
			storedEnergy -= extractAmount;
		}

		return extractAmount;
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
		return true;
	}

	@Override
	public boolean canReceive() {
		return false;
	}

	protected abstract ItemStack getChargeStack();

	protected abstract boolean isWorking();

	protected abstract boolean canWork();

	protected abstract void doWork();
}
