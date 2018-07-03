package com.sciencebitch.mod.handlers;

import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "electric_furnace");
	}

}
