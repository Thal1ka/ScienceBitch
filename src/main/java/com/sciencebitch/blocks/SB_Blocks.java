package com.sciencebitch.blocks;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.blocks.cables.BlockCableBase;
import com.sciencebitch.blocks.generators.BlockCombustionGenerator;
import com.sciencebitch.blocks.generators.BlockLavaGenerator;
import com.sciencebitch.blocks.machines.BlockElectricFurnace;
import com.sciencebitch.blocks.machines.BlockExtractor;
import com.sciencebitch.blocks.machines.BlockPulverizer;
import com.sciencebitch.blocks.transformers.Voltage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class SB_Blocks {

	public static final List<Block> BLOCKS = new ArrayList<>();

	// -------------------

	// RESOURCES
	public static final Block COPPER_ORE_BLOCK = new BlockOre("copper_ore_block", 20, 8).setHardness(4.5F);
	public static final Block COPPER_BLOCK = new BlockBase("copper_block", Material.IRON).setHardness(4.5F).setResistance(10.0F);
	public static final Block TIN_ORE_BLOCK = new BlockOre("tin_ore_block", 16, 8, 32, BlockOre.DEFAULT_MAX_HEIGHT).setHardness(8.0F);
	public static final Block TIN_BLOCK = new BlockBase("tin_block", Material.IRON).setHardness(3.5F).setResistance(8.0F);
	public static final Block NATRIUM_ORE_BLOCK = new BlockOre("natrium_ore_block", 25, 8, 56, BlockOre.DEFAULT_MAX_HEIGHT).setHardness(4.0F);
	public static final Block LEAD_ORE_BLOCK = new BlockOre("lead_ore_block", 15, 8).setHardness(4.5F);
	public static final Block LEAD_BLOCK = new BlockBase("lead_block", Material.IRON).setHardness(4.5F);
	public static final Block PLATIN_ORE = new BlockOre("platin_ore_block", 3, 8, 16, 48).setHardness(5.0F);
	public static final Block PLATIN_BLOCK = new BlockBase("platin_block", Material.IRON).setHardness(5.0F);
	public static final Block SULFUR_ORE_BLOCK = new BlockOreDustDrop("sulfur_ore_block", "sulfur_dust", 8, 8).setHardness(3.0F);
	public static final Block NITRE_ORE_BLOCK = new BlockOreDustDrop("nitre_ore_block", "nitre_dust", 4, 12, 50, BlockOre.DEFAULT_MAX_HEIGHT);

	// PLANTS
	// public static final Block CROP_HOPS = new BlockCropBase("plant_hops", null,
	// SB_Items.BATTERY, 7);

	// MACHINES
	public static final Block ELECTRIC_FURNACE = new BlockElectricFurnace("electric_furnace", false);
	public static final Block ELECTRIC_FURNACE_ACTIVE = new BlockElectricFurnace("electric_furnace_lit", true).setLightLevel(0.875F);
	public static final Block PULVERIZER = new BlockPulverizer("pulverizer", false);
	public static final Block PULVERIZER_ACTIVE = new BlockPulverizer("pulverizer_lit", true).setLightLevel(0.875F);
	public static final Block EXTRACTOR = new BlockExtractor("extractor", false);
	public static final Block EXTRACTOR_ACTIVE = new BlockExtractor("extractor_lit", true).setLightLevel(0.875F);

	// GENERATORS
	public static final Block GENERATOR_COMBUSTION = new BlockCombustionGenerator("combustion_generator", false);
	public static final Block GENERATOR_COMBUSTION_ACTIVE = new BlockCombustionGenerator("combustion_generator_lit", true).setLightLevel(0.875F);
	public static final Block GENERATOR_LAVA = new BlockLavaGenerator("lava_generator", false);
	public static final Block GENERATOR_LAVA_ACTIVE = new BlockLavaGenerator("lava_generator_lit", true).setLightLevel(0.875F);

	// CABLES
	public static final Block TIN_CABLE = new BlockCableBase("tin_cable", false, Voltage.LOW_VOLTAGE);
	public static final Block COPPER_CABLE = new BlockCableBase("copper_cable", false, Voltage.LOW_VOLTAGE);

	// public static final Block ELECTRIC_FURNACE =
	// MachineCreator.instance().getMachine(MachineCreator.ELECTRIC_FURNACE).getIdleBlock();
	// public static final Block ELECTRIC_FURNACE_ACTIVE =
	// MachineCreator.instance().getMachine(MachineCreator.ELECTRIC_FURNACE).getActiveBlock();

	// -------------------

	public static void add(Block block) {
		SB_Blocks.BLOCKS.add(block);
	}
}
