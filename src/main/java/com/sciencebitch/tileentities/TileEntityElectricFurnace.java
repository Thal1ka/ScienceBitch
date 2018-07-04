package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.BlockElectricFurnace;
import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;
import com.sciencebitch.util.EnergyHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectricFurnace extends TileEntity implements IInventory, ITickable, IEnergySink {

	public static final int ID_INPUTFIELD = 0;
	public static final int ID_FUELFIELD = 1;
	public static final int ID_OUTPUTFIELD = 2;

	public static final int ENERGY_STORAGE = 200;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	private String customName;

	private int storedEnergy;

	private int cookTime;
	private int totalCookTime;

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.electric_furnace";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.inventory) {
			if (!stack.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.get(index);
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
	public void setInventorySlotContents(int index, ItemStack stack) {

		ItemStack itemstack = this.inventory.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.inventory.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if (index == ID_INPUTFIELD && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.storedEnergy = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");

		if (compound.hasKey("CustomName", 8)) {
			this.setCustomName(compound.getString("CustomName"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.storedEnergy);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(compound, this.inventory);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}
		return compound;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean hasEnergy() {
		return this.storedEnergy > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void update() {

		boolean isBurningBeforeUpdate = hasEnergy();
		boolean updated = false;
		boolean canSmelt = canSmelt();

		if (hasEnergy()) {
			this.storedEnergy--;
		}

		if (canSmelt) {
			handleEnergy();
		}

		if (world.isRemote)
			return;

		if (canSmelt) {

			if (hasEnergy()) {

				updated = true;

				cookTime++;
				if (cookTime == totalCookTime) {
					smeltItem();
					cookTime = 0;
				}
			}
		}

		if (!canSmelt || !hasEnergy()) {
			cookTime = 0;
		}

		boolean isBurningAfterUpdate = hasEnergy();

		if (isBurningBeforeUpdate != isBurningAfterUpdate) {
			BlockElectricFurnace.setState(isBurningAfterUpdate, this.world, this.pos);
			updated = true;
		}

		if (updated) {
			this.markDirty();
		}
	}

	private void handleEnergy() {

		ItemStack fuelStack = getFuelStack();
		if (fuelStack == null || fuelStack.isEmpty())
			return;

		EnergyHelper.transferEnergy((IEnergyProvider) fuelStack.getItem(), fuelStack, this);
	}

	public int getCookTime(ItemStack input) {
		return 200;
	}

	private boolean canSmelt() {

		if (getInputStack().isEmpty())
			return false;

		ItemStack result = getSmeltingResult(getInputStack());

		if (result.isEmpty())
			return false;

		ItemStack output = getOutputStack();
		if (output.isEmpty())
			return true;
		if (!output.isItemEqual(result))
			return false;
		int res = output.getCount() + result.getCount();
		return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
	}

	public void smeltItem() {

		if (this.canSmelt()) {
			ItemStack input = getInputStack();
			ItemStack result = getSmeltingResult(input);
			ItemStack output = getOutputStack();

			if (output.isEmpty()) {
				this.inventory.set(ID_OUTPUTFIELD, result.copy());
			} else if (output.getItem() == result.getItem()) {
				output.grow(result.getCount());
			}

			input.shrink(1);
		}
	}

	private ItemStack getSmeltingResult(ItemStack input) {

		return FurnaceRecipes.instance().getSmeltingResult(input);
	}

	public static boolean isItemFuel(ItemStack fuel) {
		return fuel.getItem() instanceof IEnergyProvider;
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
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		if (index == ID_OUTPUTFIELD)
			return false;
		if (index != ID_FUELFIELD)
			return true;

		return isItemFuel(stack);
	}

	public String getGuiID() {
		return "sciencebitch:electric_furnace";
	}

	@Override
	public int getField(int id) {
		switch (id) {
			case 0:
				return this.storedEnergy;
			case 1:
				return this.ENERGY_STORAGE;
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
				System.out.println("WARNING: Tried to change the toal energy of BlockElectricFurnace");
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
	public void clear() {
		this.inventory.clear();
	}

	private ItemStack getInputStack() {
		return inventory.get(ID_INPUTFIELD);
	}

	private ItemStack getFuelStack() {
		return inventory.get(ID_FUELFIELD);
	}

	private ItemStack getOutputStack() {
		return inventory.get(ID_OUTPUTFIELD);
	}

	@Override
	public int getMaxEnergyInput() {
		return 10;
	}

	@Override
	public int getCapacityLeft(ItemStack stack) {
		return ENERGY_STORAGE - storedEnergy;
	}

	@Override
	public int injectEnergy(IEnergyProvider provider, int amount, ItemStack stack) {

		amount = Math.min(amount, getCapacityLeft(null));
		storedEnergy += amount;

		return amount;
	}
}