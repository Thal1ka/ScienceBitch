package com.sciencebitch.proxy;

import com.sciencebitch.world.generator.OreGenerator;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void initialize() {
		GameRegistry.registerWorldGenerator(OreGenerator.instance(), 0);
	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}

}
