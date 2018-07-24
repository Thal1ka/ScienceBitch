package com.sciencebitch.recipes;

@FunctionalInterface
public interface IKeyModifier<T> {

	public Object getKey(T input);
}
