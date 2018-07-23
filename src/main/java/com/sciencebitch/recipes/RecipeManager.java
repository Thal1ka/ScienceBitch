package com.sciencebitch.recipes;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.items.SB_Items;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeManager {

	public static final MachineRecipes<ItemStack> PULVERIZER_RECIPES = new MachineRecipes<>();
	public static final MachineRecipes<ItemStack> EXTRACTOR_RECIPES = new MachineRecipes<>();

	public static void initialize() {

		addCraftingRecipes();
		addSmeltingRecipes();
		addPulverizerRecipes();
	}

	/**
	 * Adds normal crafting recipes. Will be replaced by json recipes
	 */
	@Deprecated
	private static void addCraftingRecipes() {

		// SHAPED
		GameRegistry.addShapedRecipe(new ResourceLocation("copperBlock"), null, new ItemStack(SB_Blocks.COPPER_BLOCK), new String[] { "III", "III", "III" }, 'I', SB_Items.COPPER_INGOT);
		GameRegistry.addShapedRecipe(new ResourceLocation("tinBlock"), null, new ItemStack(SB_Blocks.TIN_BLOCK), new String[] { "TTT", "TTT", "TTT" }, 'T', SB_Items.TIN_INGOT);
		GameRegistry.addShapedRecipe(new ResourceLocation("leadBlock"), null, new ItemStack(SB_Blocks.LEAD_BLOCK), new String[] { "LLL", "LLL", "LLL" }, 'L', SB_Items.LEAD_INGOT);
		GameRegistry.addShapedRecipe(new ResourceLocation("leadBlock"), null, new ItemStack(SB_Blocks.PLATIN_BLOCK), new String[] { "LLL", "LLL", "LLL" }, 'L', SB_Items.PLATIN_INGOT);

		GameRegistry.addShapedRecipe(new ResourceLocation("battery"), null, new ItemStack(SB_Items.BATTERY), new String[] { "TCT", "TJT", "TTT" }, 'T', SB_Items.TIN_INGOT, 'J', SB_Items.APPLE_JUICE_BOTTLE, 'C', SB_Items.COPPER_INGOT);

		// SHAPELESS
		GameRegistry.addShapelessRecipe(new ResourceLocation("copperBlockToIngot"), null, new ItemStack(SB_Items.COPPER_INGOT, 9), getIngredient(SB_Blocks.COPPER_BLOCK));
		GameRegistry.addShapelessRecipe(new ResourceLocation("tinBlockToIngot"), null, new ItemStack(SB_Items.TIN_INGOT, 9), getIngredient(SB_Blocks.TIN_BLOCK));
		GameRegistry.addShapelessRecipe(new ResourceLocation("leadBlockToIngot"), null, new ItemStack(SB_Items.LEAD_INGOT, 9), getIngredient(SB_Blocks.LEAD_BLOCK));
		GameRegistry.addShapelessRecipe(new ResourceLocation("platinBlockToIngot"), null, new ItemStack(SB_Items.PLATIN_INGOT, 9), getIngredient(SB_Blocks.PLATIN_BLOCK));

		GameRegistry.addShapelessRecipe(new ResourceLocation("appleJuice"), null, new ItemStack(SB_Items.APPLE_JUICE_BOTTLE), getIngredient(Items.GLASS_BOTTLE), getIngredient(Items.APPLE), getIngredient(Items.APPLE), getIngredient(Items.APPLE));
		GameRegistry.addShapelessRecipe(new ResourceLocation("gunpowder"), null, new ItemStack(Items.GUNPOWDER, 5), getIngredient(SB_Items.COAL_DUST), getIngredient(SB_Items.SULFUR_DUST), getIngredient(SB_Items.NITRE_DUST), getIngredient(SB_Items.NITRE_DUST), getIngredient(SB_Items.NITRE_DUST));
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

	}

	private static Ingredient getIngredient(Block block) {
		return getIngredient(Item.getItemFromBlock(block));
	}

	private static Ingredient getIngredient(Item item) {
		return Ingredient.fromItem(item);
	}
}
