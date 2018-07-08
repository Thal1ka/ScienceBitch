package com.sciencebitch.containers;

import com.sciencebitch.tileentities.TileEntityElectricMachineBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBase extends Container {

	protected final TileEntityElectricMachineBase tileEntity;
	protected final InventoryPlayer playerInventory;

	public ContainerBase(InventoryPlayer playerInventory, TileEntityElectricMachineBase tileEntity) {

		this.playerInventory = playerInventory;
		this.tileEntity = tileEntity;
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
