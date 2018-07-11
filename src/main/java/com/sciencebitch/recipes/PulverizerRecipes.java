package com.sciencebitch.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class PulverizerRecipes {

	private static PulverizerRecipes instance;

	private Map<ItemStack, ItemStack> recipes = new HashMap<>();

	private PulverizerRecipes() {

	}

	public static PulverizerRecipes instance() {

		if (instance == null) {
			instance = new PulverizerRecipes();
		}
		return instance;
	}

	public void addRecipe(ItemStack input, ItemStack output) {
		recipes.put(input, output);
	}

	public ItemStack getRecipeResult(ItemStack input) {

		ItemStack result = recipes.get(input);

		if (result == null) {
			result = ItemStack.EMPTY;
		}

		return result;
	}

}
