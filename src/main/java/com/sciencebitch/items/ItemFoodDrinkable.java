package com.sciencebitch.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFoodDrinkable extends ItemFoodBase {

	public ItemFoodDrinkable(String name, int amount, float saturation, boolean isWolfFood) {
		super(name, amount, saturation, isWolfFood);
	}

	public ItemFoodDrinkable(String name, int amount, boolean isWolfFood) {
		super(name, amount, isWolfFood);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

		if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		if (entityplayer instanceof EntityPlayerMP) {
			CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
		}

		if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (entityplayer != null) {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	/**
	 * returns the action that specifies what animation to play when the items is
	 * being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
}
