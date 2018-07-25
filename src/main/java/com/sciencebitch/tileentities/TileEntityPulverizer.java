package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.machines.BlockPulverizer;
import com.sciencebitch.recipes.RecipeManager;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPulverizer extends TileEntityElectricMachineBase {

	public static final String NAME = "pulverizer";
	public static final int ENERGY_CAPACITY = 200;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_FUELFIELD = 1;
	public static final int ID_OUTPUTFIELD_1 = 2;
	public static final int ID_OUTPUTFIELD_2 = 3;

	private int totalCookTime, cookTime;

	public TileEntityPulverizer() {
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

		if (index == ID_OUTPUTFIELD_1 || index == ID_OUTPUTFIELD_2)
			return false;
		if (index == ID_FUELFIELD)
			return TileEntityElectricMachineBase.isItemFuel(stack);

		return true;
	}

	@Override
	public int getField(int id) {

		switch (id) {
			case 0:
				return this.storedEnergy;
			case 1:
				return this.cookTime;
			case 2:
				return this.totalCookTime;
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
				this.cookTime = value;
				break;
			case 2:
				this.totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	protected ItemStack getFuelStack() {
		return this.inventory.get(ID_FUELFIELD);
	}

	private ItemStack getInputStack() {
		return this.inventory.get(ID_INPUTFIELD);
	}

	private ItemStack getOutputStack1() {
		return this.inventory.get(ID_OUTPUTFIELD_1);
	}

	private ItemStack getOutputStack2() {
		return this.inventory.get(ID_OUTPUTFIELD_2);
	}

	@Override
	protected boolean canWork() {

		ItemStack inputStack = getInputStack();
		if (inputStack.isEmpty())
			return false;

		ItemStack workResult = getWorkResult(inputStack);
		if (workResult.isEmpty())
			return false;

		int space = getOutputSpace(getOutputStack1(), workResult);
		space += getOutputSpace(getOutputStack2(), workResult);

		return space >= workResult.getCount();
	}

	private int getOutputSpace(ItemStack outputStack, ItemStack workResult) {

		if (outputStack.isEmpty())
			return workResult.getMaxStackSize();
		if (!outputStack.isItemEqual(workResult))
			return 0;

		int maxStackSize = Math.min(this.getInventoryStackLimit(), workResult.getMaxStackSize());
		return maxStackSize - outputStack.getCount();
	}

	private ItemStack getWorkResult(ItemStack stack) {

		ItemStack result = RecipeManager.PULVERIZER_RECIPES.getRecipeResult(stack);
		return (result == null) ? ItemStack.EMPTY : result.copy();
	}

	@Override
	protected void doWork() {

		cookTime++;

		if (cookTime == totalCookTime) {
			processItem();
			cookTime = 0;
		}
	}

	@Override
	protected void onWorkCanceled() {

		cookTime = 0;
	}

	private void processItem() {

		ItemStack workResult = getWorkResult(getInputStack());
		ItemStack outputStack1 = getOutputStack1();
		ItemStack outputStack2 = getOutputStack2();

		getInputStack().shrink(1);

		int output1Space = getOutputSpace(outputStack1, workResult);
		int output2Space = getOutputSpace(outputStack2, workResult);

		int workResultHalf = (workResult.getCount() + 1) / 2;

		int addTo1 = Math.min(output1Space, workResultHalf);
		int addTo2 = Math.min(output2Space, workResultHalf);

		if (addTo1 + addTo2 > workResult.getCount()) {
			addTo2--;
		}

		if (addTo1 < workResultHalf) {
			addTo2 = workResult.getCount() - addTo1;
		} else if (addTo2 < workResultHalf) {
			addTo1 = workResult.getCount() - addTo2;
		}

		addToStack(ID_OUTPUTFIELD_1, workResult, addTo1);
		addToStack(ID_OUTPUTFIELD_2, workResult, addTo2);
	}

	private void addToStack(int outputStackId, ItemStack workResult, int amount) {

		if (amount <= 0)
			return;

		if (this.inventory.get(outputStackId).isEmpty()) {
			this.inventory.set(outputStackId, new ItemStack(workResult.getItem(), amount));
		} else {
			this.inventory.get(outputStackId).grow(amount);
		}
	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {

		BlockPulverizer.setState(isWorking, world, pos);
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
