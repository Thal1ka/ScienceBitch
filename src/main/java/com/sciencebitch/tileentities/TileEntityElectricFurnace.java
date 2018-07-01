package com.sciencebitch.tileentities;

import com.sciencebitch.blocks.BlockElectricFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityElectricFurnace extends TileEntity implements IInventory, ITickable {

	private static final int ID_INPUTSTACK = 0;
	private static final int ID_FUELSTACK = 1;
	private static final int ID_OUTPUTSTACK = 2;

	private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	private String customName;

	private int burnTime;
	private int currentBurnTime;
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
			if (!stack.isEmpty()) return false;
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
	public void setInventorySlotContents(int index, ItemStack handStack) {

		ItemStack inventoryStack = this.inventory.get(index);
		boolean sameItemInStack = !handStack.isEmpty() && handStack.isItemEqual(inventoryStack) && ItemStack.areItemStackTagsEqual(handStack, inventoryStack);
		this.inventory.set(index, handStack);

		if (handStack.getCount() > this.getInventoryStackLimit()) handStack.setCount(this.getInventoryStackLimit());

		if (index == 0 && !sameItemInStack) {

			this.totalCookTime = this.getCookTime(handStack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);

		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentBurnTime = TileEntityElectricFurnace.getItemBurnTime(this.inventory.get(2));

		if (compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);

		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(compound, this.inventory);

		if (this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isBurning() {
		return this.burnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void update() {

		boolean isBurningBeforeUpdate = this.isBurning();
		boolean updated = false;

		if (this.isBurning()) this.burnTime--;

		if (!this.world.isRemote) {

			ItemStack fuel = getFuelStack();

			if (this.isBurning() || (!fuel.isEmpty() && !getInputStack().isEmpty())) {

				if (!this.isBurning() && this.canSmelt()) {
					this.burnTime = TileEntityElectricFurnace.getItemBurnTime(fuel);
					this.currentBurnTime = this.burnTime;

					if (this.isBurning()) {
						updated = true;

						if (!fuel.isEmpty()) {
							Item itemFuel = fuel.getItem();
							fuel.shrink(1);

							if (fuel.isEmpty()) {
								ItemStack containerItemFuel = itemFuel.getContainerItem(fuel);
								this.inventory.set(TileEntityElectricFurnace.ID_FUELSTACK, containerItemFuel);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					this.cookTime++;

					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(getInputStack());
						this.smeltItem();
						updated = true;
					}

				} else {
					this.cookTime = 0;
				}

			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}
			if (isBurningBeforeUpdate != this.isBurning()) {
				updated = true;
				BlockElectricFurnace.setState(this.isBurning(), this.world, this.pos);
			}
		}
		if (updated) this.markDirty();
	}

	public int getCookTime(ItemStack input) {
		return 200;
	}

	private boolean canSmelt() {

		if (getInputStack().isEmpty()) return false;

		ItemStack result = getSmeltingResult();

		if (result.isEmpty()) return false;

		ItemStack output = getOutputStack();
		if (output.isEmpty()) return true;
		if (!output.isItemEqual(result)) return false;
		int res = output.getCount() + result.getCount();
		return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();

	}

	public void smeltItem() {

		if (this.canSmelt()) {
			ItemStack input = getInputStack();
			ItemStack result = getSmeltingResult();
			ItemStack output = getOutputStack();

			if (output.isEmpty()) {
				this.inventory.set(TileEntityElectricFurnace.ID_OUTPUTSTACK, result.copy());
			} else if (output.getItem() == result.getItem()) {
				output.grow(result.getCount());
			}

			input.shrink(1);
		}
	}

	private ItemStack getSmeltingResult() {
		return FurnaceRecipes.instance().getSmeltingResult(getInputStack());
	}

	public static int getItemBurnTime(ItemStack fuel) {

		if (fuel.isEmpty()) return 0;

		Item item = fuel.getItem();

		if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) {

			Block block = Block.getBlockFromItem(item);

			if (block == Blocks.WOODEN_SLAB) return 150;
			if (block.getDefaultState().getMaterial() == Material.WOOD) return 300;
			if (block == Blocks.COAL_BLOCK) return 16000;
		}

		if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) return 200;
		if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) return 200;
		if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) return 200;
		if (item == Items.STICK) return 100;
		if (item == Items.COAL) return 1600;
		if (item == Items.LAVA_BUCKET) return 20000;
		if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
		if (item == Items.BLAZE_ROD) return 2400;

		return GameRegistry.getFuelValue(fuel);

	}

	public static boolean isItemFuel(ItemStack fuel) {
		return TileEntityElectricFurnace.getItemBurnTime(fuel) > 0;
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

		if (index == TileEntityElectricFurnace.ID_OUTPUTSTACK) return false;
		if (index == TileEntityElectricFurnace.ID_INPUTSTACK) return true;
		return TileEntityElectricFurnace.isItemFuel(stack);
	}

	public String getGuiID() {
		return "sciencebitch:electric_furnace";
	}

	@Override
	public int getField(int id) {

		switch (id) {
			case 0:
				return this.burnTime;
			case 1:
				return this.currentBurnTime;
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
				this.burnTime = value;
				break;
			case 1:
				this.currentBurnTime = value;
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
		return inventory.get(TileEntityElectricFurnace.ID_INPUTSTACK);
	}

	private ItemStack getFuelStack() {
		return inventory.get(TileEntityElectricFurnace.ID_FUELSTACK);
	}

	private ItemStack getOutputStack() {
		return inventory.get(TileEntityElectricFurnace.ID_OUTPUTSTACK);
	}
}
