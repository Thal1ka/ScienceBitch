package com.sciencebitch.interfaces;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@FunctionalInterface
public interface ITileEntityCreator {

	public TileEntity createNewTileEntity(World worldIn, int meta);

}
