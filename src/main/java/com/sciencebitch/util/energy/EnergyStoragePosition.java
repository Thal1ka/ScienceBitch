package com.sciencebitch.util.energy;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStoragePosition {

	private final IEnergyStorage storage;
	private final BlockPos position;

	public EnergyStoragePosition(IEnergyStorage storage, BlockPos position) {

		this.storage = storage;
		this.position = position;
	}

	public IEnergyStorage getStorage() {
		return storage;
	}

	public BlockPos getPosition() {
		return position;
	}
}
