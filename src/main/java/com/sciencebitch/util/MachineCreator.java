package com.sciencebitch.util;

import java.util.HashMap;
import java.util.Map;

import com.sciencebitch.blocks.BlockMachineBaseExp;
import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.gui.GuiElectricFurnace;
import com.sciencebitch.interfaces.IContainerProvider;
import com.sciencebitch.interfaces.IGuiProvider;
import com.sciencebitch.interfaces.ITileEntityCreator;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.block.Block;

public class MachineCreator {

	private static MachineCreator instance;
	private static final Map<String, Machine> machines = new HashMap<>();

	public static final String ELECTRIC_FURNACE = "electric_furnace";

	private MachineCreator() {

		create(ELECTRIC_FURNACE, (w, m) -> new TileEntityElectricFurnace(), (i, t) -> new ContainerElectricFurnace(i, (TileEntityElectricFurnace) t), (i, t) -> new GuiElectricFurnace(i, (TileEntityElectricFurnace) t));

	}

	private void create(String name, ITileEntityCreator tileEntity, IContainerProvider container, IGuiProvider gui) {

		int guiId = ScienceBitch.getGuiHandler().registerGui(container, gui);

		Block machineIdle = new BlockMachineBaseExp(name, false, tileEntity, guiId);
		Block machineActive = new BlockMachineBaseExp(name + "_lit", true, tileEntity, guiId);

		machines.put(name, new Machine(machineIdle, machineActive));
	}

	public static Machine getMachine(String name) {
		return machines.get(name);
	}

	public static boolean initialize() {

		if (instance == null) {
			instance = new MachineCreator();
			return true;
		}
		return false;
	}

	public static void registerAll() {

		for (Machine machine : machines.values()) {
			SB_Blocks.add(machine.getActiveBlock());
			SB_Blocks.add(machine.getIdleBlock());
		}
	}

	public class Machine {

		private final Block idleBlock;
		private final Block activeBlock;

		private Machine(Block idleBlock, Block activeBlock) {

			this.idleBlock = idleBlock;
			this.activeBlock = activeBlock;
		}

		public Block getIdleBlock() {
			return idleBlock;
		}

		public Block getActiveBlock() {
			return activeBlock;
		}
	}

}
