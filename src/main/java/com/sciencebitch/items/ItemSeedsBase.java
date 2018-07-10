package com.sciencebitch.items;

import com.sciencebitch.mod.handlers.CropHandler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeedsBase extends Item implements IPlantable {

	private final String cropsBlockName;
	/** BlockID of the block the seeds can be planted on. */
	private final Block soilBlockID;

	public ItemSeedsBase(String cropsBlockName, Block soil) {

		this.cropsBlockName = cropsBlockName;
		this.soilBlockID = soil;
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack itemstack = player.getHeldItem(hand);
		IBlockState state = worldIn.getBlockState(pos);

		if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {

			Block cropBlock = CropHandler.instance().getCropBlock(cropsBlockName);
			worldIn.setBlockState(pos.up(), cropBlock.getDefaultState());

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemstack);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	@Override
	public EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {

		Block cropBlock = CropHandler.instance().getCropBlock(cropsBlockName);
		return cropBlock.getDefaultState();
	}
}