package com.sciencebitch.recipes;

import java.util.HashMap;
import java.util.Map;

public class MachineRecipes<S, T> {

	private final Map<Object, T> recipes = new HashMap<>();
	private final IKeyModifier<S> keyModifier;

	public MachineRecipes() {
		this(s -> s);
	}

	public MachineRecipes(IKeyModifier<S> keyModifier) {
		this.keyModifier = keyModifier;
	}

	public void addRecipe(S input, T output) {
		recipes.put(keyModifier.getKey(input), output);
	}

	public T getRecipeResult(S input) {

		return recipes.get(keyModifier.getKey(input));
	}

}
