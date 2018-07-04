package com.sciencebitch.items;

import com.sciencebitch.interfaces.IEnergyProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemBattery extends ItemBase implements IEnergyProvider {

	public static final int MAX_ENERGY = 200 * 4;

	public ItemBattery(String name) {
		super(name);

		setMaxDamage(MAX_ENERGY);
		setMaxStackSize(1);
	}

	@Override
	public int getMaxEnergyOutput() {
		return 40;
	}

	@Override
	public int getEnergyLeft(ItemStack stack) {
		return MAX_ENERGY - stack.getItemDamage();
	}

	@Override
	public int drainEnergy(EntityLivingBase entity, int amount, ItemStack stack) {

		amount = Math.min(amount, getEnergyLeft(stack));
		stack.damageItem(amount, entity);
		return amount;
	}
}
