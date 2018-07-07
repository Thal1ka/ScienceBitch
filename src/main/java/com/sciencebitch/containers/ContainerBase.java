package com.sciencebitch.containers;

import com.sciencebitch.tileentities.TileEntityElectricMachineBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBase extends Container {

	protected final TileEntityElectricMachineBase tileEntity;
	protected final InventoryPlayer playerInventory;

	public ContainerBase(InventoryPlayer playerInventory, TileEntityElectricMachineBase tileEntity) {

		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;

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
	public void addListener(IContainerListener listener) {

		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileEntity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {

		this.tileEntity.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileEntity.isUsableByPlayer(playerIn);
	}

}
