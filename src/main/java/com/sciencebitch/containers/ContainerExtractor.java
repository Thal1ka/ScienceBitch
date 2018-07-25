package com.sciencebitch.containers;

import com.sciencebitch.containers.slots.SlotBottleInput;
import com.sciencebitch.containers.slots.SlotElectricFuel;
import com.sciencebitch.containers.slots.SlotElectricFurnaceOutput;
import com.sciencebitch.recipes.RecipeManager;
import com.sciencebitch.tileentities.TileEntityExtractor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerExtractor extends ContainerBase {

	private int[] containerValues;

	public ContainerExtractor(InventoryPlayer playerInventory, TileEntityExtractor tileEntity) {

		super(playerInventory, tileEntity);

		containerValues = new int[tileEntity.getFieldCount()];

		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new SlotElectricFuel(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotBottleInput(tileEntity, 2, 143, 17));
		this.addSlotToContainer(new SlotElectricFurnaceOutput(playerInventory.player, tileEntity, 3, 143, 53));

		// Playerinventory
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
			}
		}

		// Single row of playerinventory
		for (int field = 0; field < 9; field++) {
			this.addSlotToContainer(new Slot(playerInventory, field, 8 + field * 18, 142));
		}
	}

	@Override
	public void detectAndSendChanges() {

		super.detectAndSendChanges();

		int[] newValues = new int[this.tileEntity.getFieldCount()];

		for (int i = 0; i < newValues.length; i++) {
			newValues[i] = this.tileEntity.getField(i);
		}

		for (IContainerListener listener : listeners) {

			for (int i = 0; i < containerValues.length; i++) {

				if (containerValues[i] != newValues[i]) {
					listener.sendWindowProperty(this, i, newValues[i]);
				}
			}
		}

		this.containerValues = newValues;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

		ItemStack stackCopy = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			ItemStack stackInSlot = slot.getStack();
			stackCopy = stackInSlot.copy();

			if (index == TileEntityExtractor.ID_OUTPUTFIELD) {

				if (!this.mergeItemStack(stackInSlot, 4, 40, true))
					return ItemStack.EMPTY;
				slot.onSlotChange(stackInSlot, stackCopy);

			} else if (index != TileEntityExtractor.ID_FUELFIELD && index != TileEntityExtractor.ID_INPUTFIELD && index != TileEntityExtractor.ID_BOTTLEFIELD) {
				// From inventory to furnace
				if (RecipeManager.EXTRACTOR_RECIPES.getRecipeResult(stackInSlot) != null) {
					if (!this.mergeItemStack(stackInSlot, 0, 1, false))
						return ItemStack.EMPTY;
				} else if (TileEntityExtractor.isItemFuel(stackInSlot)) {
					if (!this.mergeItemStack(stackInSlot, 1, 2, false))
						return ItemStack.EMPTY;
				} else if (this.isItemBottle(stackInSlot)) {
					if (!this.mergeItemStack(stackInSlot, 2, 3, false))
						return ItemStack.EMPTY;
				} else if (index >= 4 && index < 31) {
					if (!this.mergeItemStack(stackInSlot, 31, 40, false))
						return ItemStack.EMPTY;
				} else {
					if (index >= 31 && index < 40 && !this.mergeItemStack(stackInSlot, 4, 31, false))
						return ItemStack.EMPTY;
				}
			} else {
				if (!this.mergeItemStack(stackInSlot, 4, 40, false))
					return ItemStack.EMPTY;
			}

			if (stackInSlot.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stackInSlot.getCount() == stackCopy.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, stackInSlot);
		}

		return stackCopy;
	}

	private boolean isItemBottle(ItemStack stack) {
		return stack.getItem() == Items.GLASS_BOTTLE;
	}
}
