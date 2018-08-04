package com.sciencebitch.interfaces.energy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Used for energy providing items
 *
 * @author joern
 *
 */
public interface IEnergyProvider {

	int getMaxEnergyOutput();

	int getEnergyLeft(ItemStack stack);

	int drainEnergy(EntityLivingBase entity, int amount, ItemStack stack);
}
