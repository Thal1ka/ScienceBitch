package com.sciencebitch.mod.handlers;

import com.sciencebitch.tileentities.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.TileEntityExtractor;
import com.sciencebitch.tileentities.TileEntityPulverizer;
import com.sciencebitch.tileentities.generators.TileEntityCombustionGenerator;
<<<<<<< HEAD
=======
import com.sciencebitch.tileentities.machines.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.machines.TileEntityExtractor;
import com.sciencebitch.tileentities.machines.TileEntityPulverizer;
>>>>>>> parent of b76bbfe... ADD: Lava generator

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		// Machines
		GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "electric_furnace");
		GameRegistry.registerTileEntity(TileEntityPulverizer.class, "pulverizer");
		GameRegistry.registerTileEntity(TileEntityExtractor.class, "extractor");

		// Generators
<<<<<<< HEAD
		GameRegistry.registerTileEntity(TileEntityCombustionGenerator.class, "combustiongenerator");
=======
		register(TileEntityCombustionGenerator.class, "combustiongenerator");

		register(TileEntityCable.class, "cable");
	}

	private static void register(Class<? extends TileEntity> tileEntityClass, String name) {

		GameRegistry.registerTileEntity(tileEntityClass, ScienceBitch.MODID + ":" + name);
>>>>>>> parent of b76bbfe... ADD: Lava generator
	}

}
