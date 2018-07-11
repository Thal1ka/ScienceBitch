package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.machines.BlockPulverizer;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPulverizer extends TileEntityElectricMachineBase {

	public static final String NAME = "pulverizer";
	public static final int CAPACITY = 200;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_FUELFIELD = 1;
	public static final int ID_OUTPUTFIELD_1 = 2;
	public static final int ID_OUTPUTFIELD_2 = 3;

	private int totalCookTime, cookTime;

	public TileEntityPulverizer() {
		super(NAME, CAPACITY);
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
				return this.CAPACITY;
			case 2:
				return this.cookTime;
			case 3:
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
				System.err.println("WARNING: Tried to change the capacity of BlockElectricFurnace");
				break;
			case 2:
				this.cookTime = value;
				break;
			case 3:
				this.totalCookTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
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

		ItemStack smeltingResult = getSmeltingResult(inputStack);
		if (smeltingResult.isEmpty())
			return false;

		return isOutputStackValid(getOutputStack1(), smeltingResult) && isOutputStackValid(getOutputStack2(), smeltingResult);
	}

	private boolean isOutputStackValid(ItemStack outputStack, ItemStack smeltingResult) {

		if (outputStack.isEmpty())
			return true;

		if (!outputStack.isItemEqual(smeltingResult))
			return false;

		int stackSize = outputStack.getCount() + smeltingResult.getCount();
		return stackSize <= getInventoryStackLimit() && stackSize <= outputStack.getMaxStackSize();

	}

	private ItemStack getSmeltingResult(ItemStack stack) {
		return FurnaceRecipes.instance().getSmeltingResult(stack);
	}

	@Override
	protected void doWork() {

		cookTime++;

		if (cookTime == totalCookTime) {
			smeltItem();
			cookTime = 0;
		}
	}

	@Override
	protected void onWorkCanceled() {

		cookTime = 0;
	}

	private void smeltItem() {

		if (canWork()) {
			ItemStack smeltingResult = getSmeltingResult(getInputStack());
			ItemStack outputStack1 = getOutputStack1();
			ItemStack outputStack2 = getOutputStack2();

			if (outputStack1.isEmpty()) {
				this.inventory.set(ID_OUTPUTFIELD_1, smeltingResult.copy());
			} else {
				outputStack1.grow(smeltingResult.getCount());
			}

			if (outputStack2.isEmpty()) {
				this.inventory.set(ID_OUTPUTFIELD_2, smeltingResult.copy());
			} else {
				outputStack2.grow(smeltingResult.getCount());
			}

			getInputStack().shrink(1);
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
