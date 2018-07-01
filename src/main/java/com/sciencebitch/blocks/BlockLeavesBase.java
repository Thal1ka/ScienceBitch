package com.sciencebitch.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockLeavesBase extends BlockBase {

	private final Item fruit;

	public BlockLeavesBase(String name, Item fruit) {

		super(name, Material.PLANTS);

		this.fruit = fruit;
	}

}
