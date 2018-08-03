package com.sciencebitch.interfaces.energy;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Used for energy receiving items
 *
 * @author joern
 *
 */
public interface IEnergyReceiver {

	int getMaxEnergyInput();

	int getCapacityLeft(ItemStack stack);

	int injectEnergy(EntityLivingBase entity, int amount, ItemStack stack);

}
