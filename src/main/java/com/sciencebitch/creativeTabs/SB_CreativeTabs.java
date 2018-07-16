package com.sciencebitch.creativeTabs;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.items.SB_Items;

import net.minecraft.item.ItemStack;

public class SB_CreativeTabs {

	public static final CreativeTabBase TAB_MACHINES = new CreativeTabBase("machines");
	public static final CreativeTabBase TAB_RESOURCES = new CreativeTabBase("resources");
	public static final CreativeTabBase TAB_ITEMS = new CreativeTabBase("items");
	public static final CreativeTabBase TAB_ELECTRIC_ITEMS = new CreativeTabBase("electric_items");

	public static void initializeTabIcons() {

		TAB_MACHINES.setIcon(new ItemStack(SB_Blocks.ELECTRIC_FURNACE));
		TAB_RESOURCES.setIcon(new ItemStack(SB_Blocks.LEAD_ORE_BLOCK));
		TAB_ITEMS.setIcon(new ItemStack(SB_Items.TIN_INGOT));
		TAB_ELECTRIC_ITEMS.setIcon(new ItemStack(SB_Items.BATTERY));
	}

}
