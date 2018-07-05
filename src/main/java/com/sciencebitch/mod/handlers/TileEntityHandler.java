package com.sciencebitch.mod.handlers;

import com.sciencebitch.tileentities.TileEntityElectricFurnaceNew;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		GameRegistry.registerTileEntity(TileEntityElectricFurnaceNew.class, "electric_furnace");
	}

}
