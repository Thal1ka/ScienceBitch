package com.sciencebitch.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CrossReferenceRegister {

	private static final Map<String, Block> registeredBlocks = new HashMap<>();
	private static final Map<String, Item> registeredItems = new HashMap<>();

	public static void registerBlock(Block block) {
		registeredBlocks.put(block.getUnlocalizedName(), block);
	}

	public static void registerItem(Item item) {
		registeredItems.put(item.getUnlocalizedName(), item);
	}

	public static Block getBlock(String key) {
		return registeredBlocks.get(key);
	}

	public static Item getItem(String key) {
		return registeredItems.get(key);
	}
}
