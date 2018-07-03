package com.sciencebitch.blocks;

import com.sciencebitch.world.generator.OreGenerator;

import net.minecraft.block.material.Material;

public class BlockOre extends BlockBase {

	public static final int DEFAULT_MIN_HEIGHT = 0;
	public static final int DEFAULT_MAX_HEIGHT = 64;
	// a6bcbc
	private final int minSpawnHeight;
	private final int maxSpawnHeight;
	private final int veinSize;
	private final int veinAmount;

	public BlockOre(String name, Material material, int veinAmount, int veinSize) {
		this(name, material, veinAmount, veinSize, 0, 64);
	}

	public BlockOre(String name, Material material, int veinAmount, int veinSize, int minHeight, int maxHeight) {
		super(name, material);

		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException(String.format("Can not create ore with minHeight %d and maxHeight %d", minHeight, maxHeight));

		this.veinAmount = veinAmount;
		this.veinSize = veinSize;
		this.minSpawnHeight = minHeight;
		this.maxSpawnHeight = maxHeight;

		OreGenerator.addBlock(this);
	}

	public int getMinSpawnHeight() {
		return minSpawnHeight;
	}

	public int getMaxSpawnHeight() {
		return maxSpawnHeight;
	}

	public int getVeinSize() {
		return veinSize;
	}

	public int getVeinAmount() {
		return veinAmount;
	}
}
