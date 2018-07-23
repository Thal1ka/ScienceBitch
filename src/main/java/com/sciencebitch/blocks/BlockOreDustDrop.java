package com.sciencebitch.blocks;

import java.util.Random;

import com.sciencebitch.util.CrossReferenceRegister;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOreDustDrop extends BlockOre {

	private final String dropName;

	private int dropQuantity = 4;
	private int randomDropQuantity = 2;

	public BlockOreDustDrop(String name, String dropName, int veinAmount, int veinSize) {
		super(name, veinAmount, veinSize);

		this.dropName = dropName;
	}

	public BlockOreDustDrop(String name, String dropName, int veinAmount, int veinSize, int minHeight, int maxHeight) {
		super(name, veinAmount, veinSize, minHeight, maxHeight);

		this.dropName = dropName;
	}

	public BlockOreDustDrop setDropQuantity(int quantity, int randomQuantity) {

		this.dropQuantity = quantity;
		this.randomDropQuantity = randomQuantity;

		return this;
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return CrossReferenceRegister.getItem(dropName);
	}

	/**
	 * Get the quantity dropped based on the given fortune level
	 */
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return this.quantityDropped(random) + random.nextInt(fortune + 1);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return dropQuantity + random.nextInt(randomDropQuantity);
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
	}
}
