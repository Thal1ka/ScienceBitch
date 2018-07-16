package com.sciencebitch.gui;

import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.containers.ContainerPulverizer;
import com.sciencebitch.mod.handlers.GuiHandler;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;
import com.sciencebitch.tileentities.TileEntityPulverizer;

public class SB_GUIs {

	public static final GuiHandler guiHandler = new GuiHandler();

	public static final int ID_ELECTRIC_FURNACE = guiHandler.registerGui((i, t) -> new ContainerElectricFurnace(i, (TileEntityElectricFurnace) t), (i, t) -> new GuiElectricFurnace(i, (TileEntityElectricFurnace) t));
	public static final int ID_PULVERIZER = guiHandler.registerGui((i, t) -> new ContainerPulverizer(i, (TileEntityPulverizer) t), (i, t) -> new GuiPulverizer(i, (TileEntityPulverizer) t));
	// private static final int ID_Pulverizer =
	// ScienceBitch.getGuiHandler().registerGui((i, t) -> new ContainerPulverizer(i,
	// (TileEntityPulverizer) t), (i, t) -> new GuiPulverizer(i,
	// (TileEntityPulverizer) t));

}
