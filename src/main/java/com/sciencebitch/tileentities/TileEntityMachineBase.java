package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.machines.BlockMachineBase;
import com.sciencebitch.util.BlockHelper;
import com.sciencebitch.util.BlockHelper.BlockSide;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public abstract class TileEntityMachineBase extends TileEntity implements ISidedInventory {

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

	@Override
	public int[] getSlotsForFace(EnumFacing side) {

		IBlockState blockstate = world.getBlockState(getPos());
		PropertyDirection facingProperty = ((BlockMachineBase) blockstate.getBlock()).FACING;
		EnumFacing blockFacing = blockstate.getValue(facingProperty);
		BlockSide blockSide = BlockHelper.getBlockSide(blockFacing, side);

		return getSlotsForSide(blockSide);
	}

	protected boolean canAddToSlot(ItemStack slotStack, ItemStack addStack) {

		if (slotStack.isEmpty() || addStack.isEmpty()) return true;
		if (!slotStack.isItemEqual(addStack) || ItemStack.areItemStackTagsEqual(slotStack, addStack)) return false;

		int count = slotStack.getCount() + addStack.getCount();
		return count <= Math.min(slotStack.getMaxStackSize(), getInventoryStackLimit());
	}

	protected abstract void updateState(boolean isWorking, World world, BlockPos pos);

	protected abstract void readData(NBTTagCompound nbt);

	protected abstract NBTTagCompound writeData(NBTTagCompound nbt);

	protected abstract int[] getSlotsForSide(BlockSide side);
}
