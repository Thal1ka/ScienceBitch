package com.sciencebitch.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MachineRecipes {

	private Map<Item, ItemStack> recipes = new HashMap<>();

	public void addRecipe(ItemStack input, ItemStack output) {
		recipes.put(input.getItem(), output);
	}

	public ItemStack getRecipeResult(Item input) {

		ItemStack result = recipes.get(input);

		if (result == null) {
			result = ItemStack.EMPTY;
		}

		return result;
	}

}
