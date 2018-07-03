package com.sciencebitch.mod.handlers;

import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.gui.GuiElectricFurnace;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		// id = gui id
		if (ID == 0) return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (ID == 0) return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}

}
