package com.sciencebitch.blocks;

import java.util.ArrayList;
import java.util.List;

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
	public static final Block LEMON_LEAVES_BLOCK = new BlockLeavesBase("lemon_leaves_block", SB_Items.COPPER_INGOT);

	// MACHINES
	public static final Block ELECTRIC_FURNACE = new BlockElectricFurnace("electric_furnace");

	// -------------------

	public static void add(Block block) {
		SB_Blocks.BLOCKS.add(block);
	}
}
