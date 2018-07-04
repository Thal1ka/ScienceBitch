package com.sciencebitch.interfaces;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IEnergyProvider {

	public int getMaxEnergyOutput();

	public int getEnergyLeft(@Nullable ItemStack stack);

	public int drainEnergy(EntityLivingBase entity, int amount, @Nullable ItemStack stack);

}
