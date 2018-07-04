package com.sciencebitch.mod;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.items.SB_Items;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeManager {

	protected static void initialize() {

		RecipeManager.addCraftingRecipes();
		RecipeManager.addSmeltingRecipes();
	}

	private static void addCraftingRecipes() {

		GameRegistry.addShapedRecipe(new ResourceLocation("copperBlock"), null, new ItemStack(SB_Blocks.COPPER_BLOCK), new String[] { "III", "III", "III" }, 'I', SB_Items.COPPER_INGOT);
		GameRegistry.addShapedRecipe(new ResourceLocation("tinBlock"), null, new ItemStack(SB_Blocks.TIN_BLOCK), new String[] { "TTT", "TTT", "TTT" }, 'T', SB_Items.TIN_INGOT);
		GameRegistry.addShapedRecipe(new ResourceLocation("battery"), null, new ItemStack(SB_Items.BATTERY), new String[] { "TCT", "TJT", "TTT" }, 'T', SB_Items.TIN_INGOT, 'J', SB_Items.APPLE_JUICE_BOTTLE, 'C', SB_Items.COPPER_INGOT);

		GameRegistry.addShapelessRecipe(new ResourceLocation("copperBlockToIngot"), null, new ItemStack(SB_Items.COPPER_INGOT, 9), RecipeManager.getIngredient(SB_Blocks.COPPER_BLOCK));
		GameRegistry.addShapelessRecipe(new ResourceLocation("tinBlockToIngot"), null, new ItemStack(SB_Items.TIN_INGOT, 9), RecipeManager.getIngredient(SB_Blocks.TIN_BLOCK));
		GameRegistry.addShapelessRecipe(new ResourceLocation("appleJuice"), null, new ItemStack(SB_Items.APPLE_JUICE_BOTTLE), RecipeManager.getIngredient(Items.GLASS_BOTTLE), RecipeManager.getIngredient(Items.APPLE),
				RecipeManager.getIngredient(Items.APPLE), RecipeManager.getIngredient(Items.APPLE));
	}

	private static void addSmeltingRecipes() {

		GameRegistry.addSmelting(SB_Blocks.COPPER_ORE_BLOCK, new ItemStack(SB_Items.COPPER_INGOT), 0.7F);
		GameRegistry.addSmelting(SB_Blocks.TIN_ORE_BLOCK, new ItemStack(SB_Items.TIN_INGOT), 0.5F);
	}

	private static Ingredient getIngredient(Block block) {
		return Ingredient.fromItem(Item.getItemFromBlock(block));
	}

	private static Ingredient getIngredient(Item item) {
		return Ingredient.fromItem(item);
	}
}
