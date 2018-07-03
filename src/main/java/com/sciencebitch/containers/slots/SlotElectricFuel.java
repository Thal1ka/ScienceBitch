package com.sciencebitch.containers.slots;

import com.sciencebitch.util.IEnergyProvider;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotElectricFuel extends Slot {

	public SlotElectricFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {

		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return stack.getItem() instanceof IEnergyProvider;
	}

}
