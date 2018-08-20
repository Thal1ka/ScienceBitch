package com.sciencebitch.recipes;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.items.SB_Items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeManager {

	public static final MachineRecipes<ItemStack, ItemStack> PULVERIZER_RECIPES = new MachineRecipes<>(stack -> stack.getItem());
	public static final MachineRecipes<ItemStack, FluidStack> EXTRACTOR_RECIPES = new MachineRecipes<>(stack -> stack.getItem());

	public static void initialize() {

		addSmeltingRecipes();
		addPulverizerRecipes();
		addExtractorRecipes();
	}

	private static void addSmeltingRecipes() {

		GameRegistry.addSmelting(SB_Blocks.COPPER_ORE_BLOCK, new ItemStack(SB_Items.COPPER_INGOT), 0.7F);
		GameRegistry.addSmelting(SB_Blocks.TIN_ORE_BLOCK, new ItemStack(SB_Items.TIN_INGOT), 0.5F);
		GameRegistry.addSmelting(SB_Blocks.NATRIUM_ORE_BLOCK, new ItemStack(SB_Items.NATRIUM_CHUNK), 0.25F);
		GameRegistry.addSmelting(SB_Blocks.LEAD_ORE_BLOCK, new ItemStack(SB_Items.LEAD_INGOT), 0.6F);
		GameRegistry.addSmelting(SB_Blocks.PLATIN_ORE, new ItemStack(SB_Items.PLATIN_INGOT), 1.0F);

		GameRegistry.addSmelting(SB_Items.COPPER_DUST, new ItemStack(SB_Items.COPPER_INGOT), 0.7F);
		GameRegistry.addSmelting(SB_Items.TIN_DUST, new ItemStack(SB_Items.TIN_INGOT), 0.5F);
		GameRegistry.addSmelting(SB_Items.LEAD_DUST, new ItemStack(SB_Items.LEAD_INGOT), 0.6F);
		GameRegistry.addSmelting(SB_Items.PLATIN_DUST, new ItemStack(SB_Items.PLATIN_INGOT), 1.0F);

		GameRegistry.addSmelting(SB_Items.IRON_DUST, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(SB_Items.GOLD_DUST, new ItemStack(Items.GOLD_INGOT), 1.0F);
	}

	private static void addPulverizerRecipes() {

		PULVERIZER_RECIPES.addRecipe(new ItemStack(Items.DIAMOND), new ItemStack(Items.COAL, 8));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.STONE), new ItemStack(Blocks.GRAVEL));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.GRAVEL));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.GRAVEL), new ItemStack(Blocks.SAND));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.SANDSTONE), new ItemStack(Blocks.SAND, 4));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Items.COAL), new ItemStack(SB_Items.COAL_DUST, 2));

		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.IRON_ORE), new ItemStack(SB_Items.IRON_DUST, 2));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(Blocks.GOLD_ORE), new ItemStack(SB_Items.GOLD_DUST, 2));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(SB_Blocks.COPPER_ORE_BLOCK), new ItemStack(SB_Items.COPPER_DUST, 2));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(SB_Blocks.TIN_ORE_BLOCK), new ItemStack(SB_Items.TIN_DUST, 2));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(SB_Blocks.LEAD_ORE_BLOCK), new ItemStack(SB_Items.LEAD_DUST, 2));
		PULVERIZER_RECIPES.addRecipe(new ItemStack(SB_Blocks.PLATIN_ORE), new ItemStack(SB_Items.PLATIN_DUST, 2));
	}

	private static void addExtractorRecipes() {

		EXTRACTOR_RECIPES.addRecipe(new ItemStack(SB_Items.BATTERY), new FluidStack(FluidRegistry.WATER, 1));
		EXTRACTOR_RECIPES.addRecipe(new ItemStack(Blocks.COBBLESTONE), new FluidStack(FluidRegistry.LAVA, 2));
	}
}
