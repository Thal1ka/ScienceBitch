package com.sciencebitch.tileentities;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public abstract class TileEntityElectricMachineBase extends TileEntity implements IInventory, ITickable, IEnergySink {

	private final String name;
	protected String customName;

	protected final NonNullList<ItemStack> inventory;

	private int maxEnergyInput = 20;
	private final int energyCapacity;

	protected int storedEnergy;

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
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
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
		boolean updated = false;
		boolean canWork = canWork();

		if (hasEnergy()) {
			this.storedEnergy--;
		}

		if (canWork) {
			handleEnergy();
		}

		if (world.isRemote)
			return;

		if (canWork && hasEnergy()) {
			doWork();
		}

		if (!canWork || !hasEnergy()) {
			onWorkCanceled();
		}

		boolean isWorkingAfterUpdate = hasEnergy();

		if (isWorkingBeforeUpdate != isWorkingAfterUpdate) {
			updateState(isWorkingAfterUpdate, this.world, this.pos);
			updated = true;
		}

		if (updated) {
			this.markDirty();
		}
	}

	protected boolean hasEnergy() {
		return storedEnergy > 0;
	}

	private void handleEnergy() {

		ItemStack fuelStack = getFuelStack();
		if (fuelStack.isEmpty())
			return;

		EnergyHelper.transferEnergy((IEnergyProvider) fuelStack.getItem(), fuelStack, this);
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
	public void clear() {
		inventory.clear();
	}

	@Override
	public final void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);
		readData(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		return writeData(compound);
	}

	public static boolean isItemFuel(ItemStack stack) {
		return (stack.getItem() instanceof IEnergyProvider);
	}

	protected abstract ItemStack getFuelStack();

	protected abstract boolean canWork();

	protected abstract void doWork();

	protected abstract void onWorkCanceled();

	protected abstract void updateState(boolean isWorking, World world, BlockPos pos);

	protected abstract void readData(NBTTagCompound nbt);

	protected abstract NBTTagCompound writeData(NBTTagCompound nbt);

}
