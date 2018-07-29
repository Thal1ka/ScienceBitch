package com.sciencebitch.containers.slots;

import com.sciencebitch.interfaces.IEnergySink;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChargable extends Slot {

	public SlotChargable(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return stack.getItem() instanceof IEnergySink;
	}
}
