package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.machines.BlockExtractor;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityExtractor extends TileEntityElectricMachineBase {

	public static final String NAME = "extractor";
	public static final int ENERGY_CAPACITY = 200;
	public static final int FLUID_CAPACITY = 200;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_FUELFIELD = 1;
	public static final int ID_OUTPUTFIELD = 2;
	public static final int ID_BOTTLEFIELD = 3;

	private FluidStack storedFluid;
	private Item currentWorkItem;
	private FluidStack currentWorkingResult;

	private int totalCookTime, cookTime;

	public TileEntityExtractor() {
		super(NAME, ENERGY_CAPACITY);
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		ItemStack itemstack = this.inventory.get(index);
		boolean sameItem = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == ID_INPUTFIELD && !sameItem) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	private int getCookTime(ItemStack stack) {
		return 200;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		if (index == ID_OUTPUTFIELD) return false;
		if (index == ID_FUELFIELD) return TileEntityElectricMachineBase.isItemFuel(stack);
		if (index == ID_BOTTLEFIELD) return (stack.getItem() == Items.GLASS_BOTTLE);

		return true;
	}

	@Override
	public int getField(int id) {

		switch (id) {
			case 0:
				return this.storedEnergy;
			case 1:
				return this.ENERGY_CAPACITY;
			case 2:
				return this.cookTime;
			case 3:
				return this.totalCookTime;
			case 4:
				return (storedFluid == null) ? 0 : this.storedFluid.amount;
			case 5:
				return this.FLUID_CAPACITY;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {

		switch (id) {
			case 0:
				this.storedEnergy = value;
				break;
			case 1:
				System.err.println("WARNING: Tried to change the energy capacity of BlockExtractor");
				break;
			case 2:
				this.cookTime = value;
				break;
			case 3:
				this.totalCookTime = value;
				break;
			case 4:
				if (storedFluid != null) {
					this.storedFluid.amount = value;
				}
				break;
			case 5:
				System.err.println("WARNING: Tried to change the fluid capacity of BlockExtractor");
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 6;
	}

	@Override
	protected ItemStack getFuelStack() {
		return this.inventory.get(ID_FUELFIELD);
	}

	private ItemStack getInputStack() {
		return this.inventory.get(ID_INPUTFIELD);
	}

	private ItemStack getOutputStack() {
		return this.inventory.get(ID_OUTPUTFIELD);
	}

	private ItemStack getBottleStack() {
		return this.inventory.get(ID_BOTTLEFIELD);
	}

	@Override
	public void update() {

		if (currentWorkItem == null) {

			ItemStack inputStack = getInputStack();

			if (!inputStack.isEmpty()) {

				currentWorkItem = inputStack.getItem();

				if (canWork()) {
					inputStack.shrink(1);

					currentWorkingResult = getWorkingResult(currentWorkItem);

					if (storedFluid == null) {
						storedFluid = new FluidStack(currentWorkingResult.getFluid(), 0);
					}
				} else {
					currentWorkItem = null;
				}
			}
		}

		super.update();
	}

	@Override
	protected boolean canWork() {

		if (currentWorkItem == null) return false;

		FluidStack workingResult = (currentWorkingResult == null) ? getWorkingResult(currentWorkItem) : currentWorkingResult;
		if (workingResult == null) return false;

		if (storedFluid == null) return true;

		if (!storedFluid.isFluidEqual(workingResult)) return false;

		return storedFluid.amount < FLUID_CAPACITY;
	}

	private FluidStack getWorkingResult(Item currentWorkItem2) {
		return null;
	}

	@Override
	protected void doWork() {

		cookTime++;
		storedFluid.amount += currentWorkingResult.amount / totalCookTime;

		if (cookTime == totalCookTime) {
			processItem();
			cookTime = 0;
		}
	}

	@Override
	protected void onWorkCanceled() {

	}

	private void processItem() {

		if (canWork()) {
			currentWorkItem = null;
			currentWorkingResult = null;
		}
	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {

		BlockExtractor.setState(isWorking, world, pos);
	}

	@SideOnly(Side.CLIENT)
	public static boolean isWorking(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void readData(NBTTagCompound nbt) {

		this.storedEnergy = nbt.getInteger("BurnTime");
		this.cookTime = nbt.getInteger("CookTime");
		this.totalCookTime = nbt.getInteger("CookTimeTotal");
		ItemStackHelper.loadAllItems(nbt, this.inventory);

		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt) {

		nbt.setInteger("BurnTime", (short) this.storedEnergy);
		nbt.setInteger("CookTime", (short) this.cookTime);
		nbt.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(nbt, this.inventory);

		if (this.hasCustomName()) {
			nbt.setString("CustomName", this.customName);
		}

		return nbt;
	}
}