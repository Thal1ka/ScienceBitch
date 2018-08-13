package com.sciencebitch.containers;

import com.sciencebitch.tileentities.machines.TileEntityMachineBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ContainerBase extends Container {

	private int[] containerValues;

	protected final TileEntityMachineBase tileEntity;
	protected final InventoryPlayer playerInventory;

	public ContainerBase(InventoryPlayer playerInventory, TileEntityMachineBase tileEntity) {

		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;

		this.containerValues = new int[tileEntity.getFieldCount()];
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
}
