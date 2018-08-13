package com.sciencebitch.mod.handlers;

import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.cables.TileEntityCable;
import com.sciencebitch.tileentities.generators.TileEntityCombustionGenerator;
import com.sciencebitch.tileentities.machines.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.machines.TileEntityExtractor;
import com.sciencebitch.tileentities.machines.TileEntityPulverizer;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler {

	public static void registerTileEntities() {

		// Machines
		register(TileEntityElectricFurnace.class, "electric_furnace");
		register(TileEntityPulverizer.class, "pulverizer");
		register(TileEntityExtractor.class, "extractor");

		// Generators
		register(TileEntityCombustionGenerator.class, "combustiongenerator");

		register(TileEntityCable.class, "cable");
	}

	private static void register(Class<? extends TileEntity> tileEntityClass, String name) {

		GameRegistry.registerTileEntity(tileEntityClass, ScienceBitch.MODID + ":" + name);
	}

}
