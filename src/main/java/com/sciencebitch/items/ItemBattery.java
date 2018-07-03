package com.sciencebitch.items;

import com.sciencebitch.util.IEnergyProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBattery extends ItemBase implements IEnergyProvider {

	public static final int MAX_ENERGY = 12801;

	public ItemBattery(String name) {
		super(name);

		setMaxDamage(MAX_ENERGY);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack itemstack = player.getHeldItem(hand);
		itemstack.damageItem(20, player);

		return EnumActionResult.SUCCESS;
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
