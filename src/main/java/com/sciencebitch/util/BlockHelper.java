package com.sciencebitch.util;

import net.minecraft.util.EnumFacing;

public class BlockHelper {

	public static BlockSide getBlockSide(EnumFacing blockFacing, EnumFacing from) {

		if (blockFacing == EnumFacing.UP || blockFacing == EnumFacing.DOWN) return null;

		if (from == EnumFacing.UP) return BlockSide.TOP;
		if (from == EnumFacing.DOWN) return BlockSide.BOTTOM;

		int blockIndex = blockFacing.getHorizontalIndex();
		int fromIndex = from.getHorizontalIndex();

		int sideIndex = (blockIndex - fromIndex + 4) % 4;

		return getBlockSide(sideIndex);
	}

	private static BlockSide getBlockSide(int index) {

		switch (index) {
			case 0:
				return BlockSide.FRONT;
			case 1:
				return BlockSide.LEFT;
			case 2:
				return BlockSide.BACK;
			case 3:
				return BlockSide.RIGHT;
			default:
				return BlockSide.FRONT;
		}
	}

	public enum BlockSide {
		TOP, BOTTOM, LEFT, RIGHT, BACK, FRONT
	}
}
