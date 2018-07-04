package com.sciencebitch.interfaces;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public interface IContainerProvider {

	public Container getContainer(InventoryPlayer playerInventory, TileEntity tileEntity);
}
