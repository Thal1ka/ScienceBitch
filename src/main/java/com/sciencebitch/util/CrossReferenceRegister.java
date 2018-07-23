package com.sciencebitch.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CrossReferenceRegister {

	private static final Map<String, Block> registeredBlocks = new HashMap<>();
	private static final Map<String, Item> registeredItems = new HashMap<>();

	public static void registerBlock(Block block, String key) {
		registeredBlocks.put(key, block);
	}

	public static void registerItem(Item item, String key) {
		registeredItems.put(key, item);
	}

	public static Block getBlock(String key) {
		return registeredBlocks.get(key);
	}

	public static Item getItem(String key) {
		return registeredItems.get(key);
	}
}
