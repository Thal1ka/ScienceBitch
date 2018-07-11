package com.sciencebitch.mod.handlers;

import com.sciencebitch.tileentities.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.TileEntityPulverizer;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "electric_furnace");
		GameRegistry.registerTileEntity(TileEntityPulverizer.class, "pulverizer");
	}

}
