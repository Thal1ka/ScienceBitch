package com.sciencebitch.gui;

import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.containers.ContainerExtractor;
import com.sciencebitch.containers.ContainerPulverizer;
import com.sciencebitch.containers.generators.ContainerCombustionGenerator;
import com.sciencebitch.gui.generators.GuiCombustionGenerator;
import com.sciencebitch.mod.handlers.GuiHandler;
import com.sciencebitch.tileentities.generators.TileEntityCombustionGenerator;
import com.sciencebitch.tileentities.machines.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.machines.TileEntityExtractor;
import com.sciencebitch.tileentities.machines.TileEntityPulverizer;

public class SB_GUIs {

	public static final GuiHandler guiHandler = new GuiHandler();

	// Machines
	public static final int ID_ELECTRIC_FURNACE = guiHandler.registerGui((i, t) -> new ContainerElectricFurnace(i, (TileEntityElectricFurnace) t), (i, t) -> new GuiElectricFurnace(i, (TileEntityElectricFurnace) t));
	public static final int ID_PULVERIZER = guiHandler.registerGui((i, t) -> new ContainerPulverizer(i, (TileEntityPulverizer) t), (i, t) -> new GuiPulverizer(i, (TileEntityPulverizer) t));
	public static final int ID_EXTRACTOR = guiHandler.registerGui((i, t) -> new ContainerExtractor(i, (TileEntityExtractor) t), (i, t) -> new GuiExtractor(i, (TileEntityExtractor) t));

	// Generators
	public static final int ID_COMBUSTION_GENERATOR = guiHandler.registerGui((i, t) -> new ContainerCombustionGenerator(i, (TileEntityCombustionGenerator) t), (i, t) -> new GuiCombustionGenerator(i, (TileEntityCombustionGenerator) t));
}
