package com.sciencebitch.util;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class NbtHelper {

	public static boolean saveItem(Item item, String key, NBTTagCompound nbt) {

		if (nbt.hasKey(key, Constants.NBT.TAG_STRING)) return false;

		ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(item);
		String value = (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString();
		nbt.setString(key, value);

		return true;
	}

	public static Item loadItem(String key, NBTTagCompound nbt) {

		return nbt.hasKey(key, Constants.NBT.TAG_STRING) ? Item.getByNameOrId(nbt.getString(key)) : null;
	}
}
