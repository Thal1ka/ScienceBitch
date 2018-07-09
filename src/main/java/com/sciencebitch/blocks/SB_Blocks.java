package com.sciencebitch.blocks;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.blocks.machines.BlockElectricFurnace;
import com.sciencebitch.blocks.plants.crops.BlockCropBase;
import com.sciencebitch.items.SB_Items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class SB_Blocks {

	public static final List<Block> BLOCKS = new ArrayList<>();

	// -------------------

	// RESOURCES
	public static final Block COPPER_ORE_BLOCK = new BlockOre("copper_ore_block", Material.ROCK, 20, 8);
	public static final Block COPPER_BLOCK = new BlockBase("copper_block", Material.IRON).setHardness(4.5F).setResistance(10.0F);
	public static final Block TIN_ORE_BLOCK = new BlockOre("tin_ore_block", Material.ROCK, 16, 8, 32, BlockOre.DEFAULT_MAX_HEIGHT);
	public static final Block TIN_BLOCK = new BlockBase("tin_block", Material.IRON).setHardness(3.5F).setResistance(8.0F);

	// PLANTS
	public static final Block CROP_HOPS = new BlockCropBase("plant_hops", null, SB_Items.BATTERY, 7);

	// MACHINES
	public static final Block ELECTRIC_FURNACE = new BlockElectricFurnace("electric_furnace", false);
	public static final Block ELECTRIC_FURNACE_ACTIVE = new BlockElectricFurnace("electric_furnace_lit", true).setLightLevel(0.875F);

//	public static final Block ELECTRIC_FURNACE = MachineCreator.instance().getMachine(MachineCreator.ELECTRIC_FURNACE).getIdleBlock();
//	public static final Block ELECTRIC_FURNACE_ACTIVE = MachineCreator.instance().getMachine(MachineCreator.ELECTRIC_FURNACE).getActiveBlock();

	// -------------------

	public static void add(Block block) {
		SB_Blocks.BLOCKS.add(block);
	}

	private static Block getBlock() {
		return null;
	}
}
