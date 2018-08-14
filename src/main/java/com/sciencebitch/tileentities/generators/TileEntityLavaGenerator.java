package com.sciencebitch.tileentities.generators;

import com.sciencebitch.blocks.generators.BlockLavaGenerator;
import com.sciencebitch.util.BlockHelper.BlockSide;

import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityLavaGenerator extends TileEntityGeneratorBase {

	public static final String NAME = "lava_generator";
	public static final int ENERGY_CAPACITY = 3200;

	private static final int maxEnergyPerTick = 25;

	private static final int bucket_capacity = 1000;
	public static final int FLUID_CAPACITY = 10 * bucket_capacity;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_EMPTYFIELD = 1;
	public static final int ID_CHARGEFIELD = 2;

	private int fluidAmount;
	private int energyProduced;
	private int workingBuffer;

	public TileEntityLavaGenerator() {
		super(NAME, ENERGY_CAPACITY);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {

		if (index == ID_CHARGEFIELD) return TileEntityGeneratorBase.isItemChargable(stack);
		if (index == ID_EMPTYFIELD) return false;

		return isItemFuel(stack);
	}

	public static boolean isItemFuel(ItemStack stack) {
		return stack.getItem() == Items.LAVA_BUCKET;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

		if (index == ID_EMPTYFIELD) return true;
		if (index == ID_CHARGEFIELD) return isItemFullyCharged();
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		if (index == ID_CHARGEFIELD) return TileEntityGeneratorBase.isItemChargable(stack);
		if (index == ID_EMPTYFIELD) return false;

		return isItemFuel(stack);
	}

	@Override
	public int getField(int id) {

		switch (id) {
			case 0:
				return storedEnergy;
			case 1:
				return fluidAmount;
			case 2:
				return workingBuffer;
		}

		return 0;
	}

	@Override
	public void setField(int id, int value) {

		switch (id) {
			case 0:
				storedEnergy = value;
				break;
			case 1:
				fluidAmount = value;
				break;
			case 2:
				workingBuffer = value;
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	protected ItemStack getChargeStack() {
		return this.inventory.get(ID_CHARGEFIELD);
	}

	private ItemStack getInputStack() {
		return this.inventory.get(ID_INPUTFIELD);
	}

	private ItemStack getEmptyStack() {
		return this.inventory.get(ID_EMPTYFIELD);
	}

	@Override
	protected boolean isWorking() {

		if (hasFuel() && getCapacityLeft() > 0) {
			workingBuffer = 6;
			return true;
		}

		if (workingBuffer > 0) {
			workingBuffer--;
			return true;
		}

		return false;
	}

	public static boolean isWorking(TileEntityLavaGenerator tileentity) {
		return tileentity.isWorking();
	}

	@Override
	protected boolean canWork() {

		handleFuel();
		return (hasFuel() && getCapacityLeft() > 0);
	}

	private void handleFuel() {

		ItemStack fuelStack = getInputStack();
		ItemStack bucketStack = getEmptyStack();

		boolean hasCapacity = (FLUID_CAPACITY - fluidAmount) >= bucket_capacity;
		boolean spaceForBucket = bucketStack.isEmpty() || bucketStack.getCount() < bucketStack.getMaxStackSize();

		if (!fuelStack.isEmpty() && hasCapacity && spaceForBucket) {

			fluidAmount += bucket_capacity;

			this.inventory.set(ID_INPUTFIELD, ItemStack.EMPTY);

			if (bucketStack.isEmpty()) {
				ItemStack containerStack = new ItemStack(fuelStack.getItem().getContainerItem());
				this.inventory.set(ID_EMPTYFIELD, containerStack);

			} else {
				bucketStack.grow(1);
			}
		}
	}

	private boolean hasFuel() {
		return fluidAmount > 0;
	}

	@Override
	protected void doWork() {

		if (fluidAmount <= 0) return; // Can happen because of working state buffer

		int energyToGenerate = Math.min(maxEnergyPerTick, getCapacityLeft());
		energyProduced += energyToGenerate;

		if (energyProduced >= maxEnergyPerTick) {
			fluidAmount--;
			energyProduced -= maxEnergyPerTick;
		}

		generateEnergy(energyToGenerate);
	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {
		BlockLavaGenerator.setState(isWorking, world, pos);
	}

	@Override
	protected void readData(NBTTagCompound nbt) {

		fluidAmount = nbt.getInteger("fluidAmount");
		energyProduced = nbt.getInteger("energyProduced");
		workingBuffer = nbt.getInteger("workingBuffered");
		ItemStackHelper.loadAllItems(nbt, inventory);
	}

	@Override
	protected NBTTagCompound writeData(NBTTagCompound nbt) {

		nbt.setInteger("fluidAmount", fluidAmount);
		nbt.setInteger("energyProduced", energyProduced);
		nbt.setInteger("workingBuffered", workingBuffer);
		ItemStackHelper.saveAllItems(nbt, inventory);

		return nbt;
	}

	@Override
	protected int[] getSlotsForSide(BlockSide side) {

		return new int[] { ID_INPUTFIELD, ID_CHARGEFIELD, ID_EMPTYFIELD };
	}
}
