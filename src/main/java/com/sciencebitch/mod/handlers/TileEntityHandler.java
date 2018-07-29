package com.sciencebitch.mod.handlers;

import com.sciencebitch.tileentities.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.TileEntityExtractor;
import com.sciencebitch.tileentities.TileEntityPulverizer;
import com.sciencebitch.tileentities.generators.TileEntityCombustionGenerator;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		// Machines
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "electric_furnace");
		GameRegistry.registerTileEntity(TileEntityPulverizer.class, "pulverizer");
		GameRegistry.registerTileEntity(TileEntityExtractor.class, "extractor");

		// Generators
		GameRegistry.registerTileEntity(TileEntityCombustionGenerator.class, "combustiongenerator");
	}

}
