package com.sciencebitch.items;

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
import net.minecraft.world.World;

public class ItemSeedsBase extends Item implements net.minecraftforge.common.IPlantable {

	private final Block crops;
	/** BlockID of the block the seeds can be planted on. */
	private final Block soilBlockID;

	public ItemSeedsBase(Block crops, Block soil) {
		this.crops = crops;
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
			worldIn.setBlockState(pos.up(), this.crops.getDefaultState());

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemstack);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	@Override
	public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return this.crops == net.minecraft.init.Blocks.NETHER_WART ? net.minecraftforge.common.EnumPlantType.Nether : net.minecraftforge.common.EnumPlantType.Crop;
	}

	@Override
	public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return this.crops.getDefaultState();
	}
}