package com.sciencebitch.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

public class SB_Items {

	public static final List<Item> ITEMS = new ArrayList<>();

	// -------------------------

	// RESOURCES
	public static final Item COPPER_INGOT = new ItemBase("copper_ingot");
	public static final Item TIN_INGOT = new ItemBase("tin_ingot");
	public static final Item NATRIUM_CHUNK = new ItemNatriumChunk("natrium_chunk");
	public static final Item LEAD_INGOT = new ItemBase("lead_ingot");

	// FOOD
	public static final Item APPLE_JUICE_BOTTLE = new ItemFoodDrinkable("apple_juice_bottle", 4, 0.3F, false);

	// TECHNOLOGY
	public static final Item BATTERY = new ItemBattery("battery");

	// -------------------------

	public void add(Item item) {
		SB_Items.ITEMS.add(item);
	}
}
