package com.sciencebitch.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class TileEntityMachineBase extends TileEntity implements IInventory {

	private final String name;
	protected String customName;

	protected final NonNullList<ItemStack> inventory;

	public TileEntityMachineBase(String name) {

		this.name = "container." + name;

		this.inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
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
	public boolean isEmpty() {

		for (ItemStack stack : inventory) {
			if (!stack.isEmpty()) return false;
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
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);

		ItemStackHelper.loadAllItems(compound, inventory);

		if (compound.hasKey("customName", Constants.NBT.TAG_STRING)) {
			this.customName = compound.getString("customName");
		}

		readData(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		ItemStackHelper.saveAllItems(compound, inventory);

		if (this.hasCustomName()) {
			compound.setString("customName", this.customName);
		}

		return writeData(compound);
	}

	protected abstract void updateState(boolean isWorking, World world, BlockPos pos);

	protected abstract void readData(NBTTagCompound nbt);

	protected abstract NBTTagCompound writeData(NBTTagCompound nbt);

}
