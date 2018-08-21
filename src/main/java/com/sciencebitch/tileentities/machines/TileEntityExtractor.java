package com.sciencebitch.tileentities.machines;

import com.sciencebitch.blocks.machines.BlockExtractor;
import com.sciencebitch.mod.handlers.FluidHandler;
import com.sciencebitch.recipes.RecipeManager;
import com.sciencebitch.util.BlockHelper.BlockSide;
import com.sciencebitch.util.NbtHelper;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityExtractor extends TileEntityElectricMachineBase {

	public static final String NAME = "extractor";
	public static final int ENERGY_CAPACITY = 200;
	public static final int FLUID_CAPACITY = 1000;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_FUELFIELD = 1;
	public static final int ID_OUTPUTFIELD = 2;
	public static final int ID_BOTTLEFIELD = 3;

	private int fluidType = -1;
	private int fluidAmount;

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

		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	private int getCookTime(Item item) {
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
				return this.cookTime;
			case 2:
				return this.totalCookTime;
			case 3:
				return fluidAmount;
			case 4:
				return fluidType;
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
				break;
			case 3:
				this.fluidAmount = value;
				break;
			case 4:
				this.fluidType = value;
				break;
		}
	}

	@Override
	public int getFieldCount() {
		return 5;
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

					if (fluidType < 0) {
						fluidAmount = 0;
						fluidType = FluidHandler.getId(currentWorkingResult.getFluid());
					}

					this.totalCookTime = getCookTime(currentWorkItem);

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

		if (fluidType < 0) return true;

		if (fluidType != FluidHandler.getId(workingResult.getFluid())) return false;

		return fluidAmount < FLUID_CAPACITY;
	}

	private FluidStack getWorkingResult(Item currentWorkItem) {
		return RecipeManager.EXTRACTOR_RECIPES.getRecipeResult(new ItemStack(currentWorkItem));
	}

	@Override
	protected void doWork() {

		cookTime++;
		fluidAmount += currentWorkingResult.amount;

		if (cookTime == totalCookTime) {
			processItem();
			System.out.println(FluidRegistry.getFluidName(FluidHandler.getFluid(fluidType)));
			cookTime = 0;
		}
	}

	@Override
	protected void onWorkCanceled() {

	}

	private void processItem() {

		currentWorkItem = null;
		currentWorkingResult = null;
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

		this.currentWorkItem = NbtHelper.loadItem("CurrentWorkItem", nbt);
		this.currentWorkingResult = getWorkingResult(currentWorkItem);

		this.fluidType = nbt.getInteger("fluidType");
		this.fluidAmount = nbt.getInteger("fluidAmount");

	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound nbt) {

		nbt.setInteger("BurnTime", (short) this.storedEnergy);
		nbt.setInteger("CookTime", (short) this.cookTime);
		nbt.setInteger("CookTimeTotal", (short) this.totalCookTime);

		NbtHelper.saveItem(currentWorkItem, "CurrentWorkItem", nbt);

		nbt.setInteger("fluidType", this.fluidType);
		nbt.setInteger("fluidAmount", this.fluidAmount);

		return nbt;
	}

	private void readFluidFromNbt(NBTTagCompound nbt) {

	}

	private void writeFluidToNbt(NBTTagCompound nbt) {

	}

	@Override
	protected int[] getSlotsForSide(BlockSide side) {

		return new int[] { ID_INPUTFIELD, ID_FUELFIELD, ID_OUTPUTFIELD, ID_BOTTLEFIELD };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

		if (index == ID_OUTPUTFIELD) return false;
		if (index == ID_FUELFIELD) return isItemValidForSlot(index, itemStackIn);
		if (index == ID_BOTTLEFIELD) return isItemValidForSlot(index, itemStackIn);

		ItemStack stackInSlot = this.inventory.get(index);

		return canAddToSlot(stackInSlot, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

		if (index == ID_INPUTFIELD || index == ID_BOTTLEFIELD) return false;
		if (index == ID_FUELFIELD) return isFuelEmpty();

		return true;
	}
}
