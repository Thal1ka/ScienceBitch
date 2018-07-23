package com.sciencebitch.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MachineRecipes<T> {

	private Map<Item, T> recipes = new HashMap<>();

	public void addRecipe(ItemStack input, T output) {
		recipes.put(input.getItem(), output);
	}

	public T getRecipeResult(Item input) {

		return recipes.get(input);
	}

}
