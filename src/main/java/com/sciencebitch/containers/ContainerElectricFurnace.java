package com.sciencebitch.containers;

import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerElectricFurnace extends Container {

	private final TileEntityElectricFurnace tileEntity;
	private int cookTime, totalCookTime, burnTime, currentBurnTime;

	public ContainerElectricFurnace(InventoryPlayer playerInventory, TileEntityElectricFurnace tileEntity) {

		this.tileEntity = tileEntity;

		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 17));
		this.addSlotToContainer(new SlotFurnaceFuel(tileEntity, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, tileEntity, 2, 116, 35));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return false;
	}

}
