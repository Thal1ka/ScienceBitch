package com.sciencebitch.mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.sciencebitch.blocks.BlockOre;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGenerator implements IWorldGenerator {

	private static OreGenerator instance;
	private static final Map<WorldGenerator, BlockOre> generators = new HashMap<>();

	private OreGenerator() {

	}

	public static OreGenerator instance() {

		if (OreGenerator.instance == null) {
			OreGenerator.instance = new OreGenerator();
		}
		return OreGenerator.instance;
	}

	public static void addBlock(BlockOre ore) {

		OreGenerator.generators.put(new WorldGenMinable(ore.getDefaultState(), ore.getVeinSize()), ore);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

		if (world.provider.getDimension() != 0)
			return;

		for (Entry<WorldGenerator, BlockOre> entry : OreGenerator.generators.entrySet()) {

			WorldGenerator generator = entry.getKey();
			BlockOre ore = entry.getValue();

			for (int i = 0; i < ore.getVeinAmount(); i++) {

				BlockPos position = getRandomBlockPos(random, chunkX, chunkZ, ore.getMinSpawnHeight(), ore.getMaxSpawnHeight());
				generator.generate(world, random, position);
			}
		}
	}

	private BlockPos getRandomBlockPos(Random rnd, int chunkX, int chunkZ, int minHeight, int maxHeight) {

		int x = chunkX * 16 + rnd.nextInt(16);
		int y = minHeight + rnd.nextInt(maxHeight - minHeight);
		int z = chunkZ * 16 + rnd.nextInt(16);

		return new BlockPos(x, y, z);
	}

}
