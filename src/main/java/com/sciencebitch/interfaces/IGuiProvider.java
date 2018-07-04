package com.sciencebitch.interfaces;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

@FunctionalInterface
public interface IGuiProvider {

	public GuiContainer getGui(InventoryPlayer playerInventory, TileEntity tileEntity);
}
