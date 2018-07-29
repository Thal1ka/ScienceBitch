package com.sciencebitch.tileentities.generators;

import com.sciencebitch.blocks.machines.generators.BlockCombustionGenerator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCombustionGenerator extends TileEntityGeneratorBase {

	public static final String NAME = "combustion_generator";
	public static final int ENERGY_CAPACITY = 1600;

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_CURRENT_ITEMFIELD = 1;
	public static final int ID_CHARGEFIELD = 2;

	private int currentBurnTime;
	private int totalBurnTime;

	private int speedMultiplicator = 1;

	public TileEntityCombustionGenerator() {

		super(NAME, ENERGY_CAPACITY);
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

		if (index == ID_CURRENT_ITEMFIELD) return false;
		if (index == ID_CHARGEFIELD) return TileEntityGeneratorBase.isItemChargable(stack);

		return true;
	}

	@Override
	public int getField(int id) {

		switch (id) {
			case 0:
				return currentBurnTime;
			case 1:
				return totalBurnTime;
			case 2:
				return storedEnergy;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value) {

		switch (id) {
			case 0:
				this.currentBurnTime = value;
				break;
			case 1:
				this.totalBurnTime = value;
				break;
			case 2:
				this.storedEnergy = value;
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

	private ItemStack getCurrentItemStack() {
		return this.inventory.get(ID_CURRENT_ITEMFIELD);
	}

	public static boolean isWorking(TileEntityCombustionGenerator tileEntity) {
		return tileEntity.isWorking();
	}

	@Override
	protected boolean isWorking() {
		return totalBurnTime > 0;
	}

	@Override
	protected boolean canWork() {

		if (totalBurnTime > 0) return true;

		return getCapacityLeft() > 0 && !getCurrentItemStack().isEmpty();
	}

	@Override
	protected void doWork() {

		if (totalBurnTime <= 0) {
			totalBurnTime = getItemBurnDuration(getCurrentItemStack());
		}

		generateEnergy(speedMultiplicator);
		currentBurnTime += speedMultiplicator;

		if (currentBurnTime >= totalBurnTime) {
			currentBurnTime = 0;
			totalBurnTime = 0;
			consumeCurrentItem();
		}
	}

	private void consumeCurrentItem() {

		this.inventory.set(ID_CURRENT_ITEMFIELD, ItemStack.EMPTY);
	}

	@Override
	public void update() {

		if (!isWorking()) {

			ItemStack inputStack = getInputStack();

			if (!inputStack.isEmpty()) {
				if (getItemBurnDuration(inputStack) > 0 && getCapacityLeft() > 0) {
					ItemStack currentItem = new ItemStack(inputStack.getItem(), 1, inputStack.getMetadata());
					this.inventory.set(ID_CURRENT_ITEMFIELD, currentItem);
					inputStack.shrink(1);
				}
			}
		}

		super.update();
	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {
		BlockCombustionGenerator.setState(isWorking, world, pos);
	}

	@Override
	protected void readData(NBTTagCompound nbt) {

		this.currentBurnTime = nbt.getInteger("currentBurnTime");
		this.totalBurnTime = nbt.getInteger("totalBurnTime");
	}

	@Override
	protected NBTTagCompound writeData(NBTTagCompound nbt) {

		nbt.setInteger("currentBurnTime", currentBurnTime);
		nbt.setInteger("totalBurnTime", totalBurnTime);

		return nbt;
	}

	private static int getItemBurnDuration(ItemStack stack) {

		if (stack.getItem().hasContainerItem(stack)) return 0;
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	public static boolean isItemFuel(ItemStack stack) {
		return getItemBurnDuration(stack) > 0;
	}
}
