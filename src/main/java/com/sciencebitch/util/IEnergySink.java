package com.sciencebitch.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public interface IEnergySink {

	public int getMaxEnergyInput();

	public int getCapacityLeft(@Nullable ItemStack stack);

	public int injectEnergy(IEnergyProvider provider, int amount, @Nullable ItemStack stack);

}
