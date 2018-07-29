package com.sciencebitch.tileentities.generators;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCombustionGenerator extends TileEntityGeneratorBase {

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_CURRENT_ITEMFIELD = 1;
	public static final int ID_CHARGEFIELD = 2;

	private int currentBurnTime;
	private int totalBurnTime;

	private int speedMultiplicator = 1;

	public TileEntityCombustionGenerator(String name, int energyCapacity) {

		super(name, energyCapacity);
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
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
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
				if (getItemBurnDuration(inputStack) > 0) {
					this.inventory.set(ID_CURRENT_ITEMFIELD, new ItemStack(inputStack.getItem()));
					inputStack.shrink(1);
				}
			}
		}

		super.update();
	}

	@Override
	protected void updateState(boolean isWorking, World world, BlockPos pos) {

	}

	@Override
	protected void readData(NBTTagCompound nbt) {

	}

	@Override
	protected NBTTagCompound writeData(NBTTagCompound nbt) {
		return null;
	}

	private int getItemBurnDuration(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack);
	}
}
