package com.sciencebitch.mod.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sciencebitch.interfaces.IBlockHandler;
import com.sciencebitch.interfaces.IItemHandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class CropHandler implements IBlockHandler, IItemHandler {

	private static CropHandler instance;
	private static final Map<String, Block> cropBlocks = new HashMap<>();
	private static final Map<String, Item> seedItems = new HashMap<>();

	private CropHandler() {

	}

	public Block getCropBlock(String name) {
		return cropBlocks.get(name);
	}

	public Item getSeedItem(String name) {
		return seedItems.get(name);
	}

	@Override
	public List<Block> getBlocksToRegister() {
		return new ArrayList<>(cropBlocks.values());
	}

	@Override
	public List<Item> getItemsToRegister() {
		return new ArrayList<>(seedItems.values());
	}

	public static CropHandler instance() {

		if (instance == null) {
			instance = new CropHandler();

			RegistryHandler.registerBlockHandler(instance);
			RegistryHandler.registerItemHandler(instance);
		}

		return instance;
	}
}
