package com.sciencebitch.containers;

import com.sciencebitch.containers.slots.SlotElectricFuel;
import com.sciencebitch.tileentities.machines.TileEntityElectricFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerElectricFurnace extends ContainerBase {

	public ContainerElectricFurnace(InventoryPlayer playerInventory, TileEntityElectricFurnace tileEntity) {

		super(playerInventory, tileEntity);

		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new SlotElectricFuel(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, tileEntity, 2, 116, 35));

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
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

		ItemStack stackCopy = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			ItemStack stackInSlot = slot.getStack();
			stackCopy = stackInSlot.copy();

			if (index == TileEntityElectricFurnace.ID_OUTPUTFIELD) {

				if (!this.mergeItemStack(stackInSlot, 3, 39, true)) return ItemStack.EMPTY;
				slot.onSlotChange(stackInSlot, stackCopy);

			} else if (index != TileEntityElectricFurnace.ID_FUELFIELD && index != TileEntityElectricFurnace.ID_INPUTFIELD) {
				// From inventory to furnace
				if (!FurnaceRecipes.instance().getSmeltingResult(stackInSlot).isEmpty()) {
					if (!this.mergeItemStack(stackInSlot, 0, 1, false)) return ItemStack.EMPTY;
				} else if (TileEntityElectricFurnace.isItemFuel(stackInSlot)) {
					if (!this.mergeItemStack(stackInSlot, 1, 2, false)) return ItemStack.EMPTY;
				} else if (index >= 3 && index < 30) {
					if (!this.mergeItemStack(stackInSlot, 30, 39, false)) return ItemStack.EMPTY;
				} else {
					if (index >= 30 && index < 39 && !this.mergeItemStack(stackInSlot, 3, 30, false)) return ItemStack.EMPTY;
				}

			} else {
				if (!this.mergeItemStack(stackInSlot, 3, 39, false)) return ItemStack.EMPTY;
			}

			if (stackInSlot.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (stackInSlot.getCount() == stackCopy.getCount()) return ItemStack.EMPTY;

			slot.onTake(playerIn, stackInSlot);
		}

		return stackCopy;
	}
}
