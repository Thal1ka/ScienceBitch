package com.sciencebitch.blocks.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sciencebitch.containers.ContainerElectricFurnace;
import com.sciencebitch.gui.GuiElectricFurnace;
import com.sciencebitch.interfaces.IContainerProvider;
import com.sciencebitch.interfaces.IGuiProvider;
import com.sciencebitch.interfaces.ITileEntityCreator;
import com.sciencebitch.mod.ScienceBitch;
import com.sciencebitch.tileentities.TileEntityElectricFurnace;

import net.minecraft.block.Block;

public class MachineCreator {

	private final boolean disable = true;

	private static MachineCreator instance;
	private static final Map<String, Machine> machines = new HashMap<>();

	public static final String ELECTRIC_FURNACE = "electric_furnace";

	private MachineCreator() {

		if (disable)
			return;

		create(ELECTRIC_FURNACE, (w, m) -> new TileEntityElectricFurnace(), (i, t) -> new ContainerElectricFurnace(i, (TileEntityElectricFurnace) t), (i, t) -> new GuiElectricFurnace(i, (TileEntityElectricFurnace) t));

	}

	public static MachineCreator instance() {

		if (instance == null) {
			instance = new MachineCreator();
		}

		return instance;
	}

	private void create(String name, ITileEntityCreator tileEntity, IContainerProvider container, IGuiProvider gui) {

		int guiId = ScienceBitch.getGuiHandler().registerGui(container, gui);

		Block machineIdle = new BlockMachineBaseExp(name, false, tileEntity, guiId);
		Block machineActive = new BlockMachineBaseExp(name + "_lit", true, tileEntity, guiId);

		machines.put(name, new Machine(machineIdle, machineActive));
	}

	public Machine getMachine(String name) {
		return machines.get(name);
	}

	public List<Block> getBlocksToRegister() {

		List<Block> blocks = new ArrayList<>();

		for (Machine machine : machines.values()) {
			blocks.add(machine.getActiveBlock());
			blocks.add(machine.getIdleBlock());
		}

		return blocks;
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
