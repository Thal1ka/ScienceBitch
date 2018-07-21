package com.sciencebitch.mod.handlers;

import com.sciencebitch.entity.items.EntityItemNatriumChunk;
import com.sciencebitch.items.SB_Items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class DropHandler {

	@SubscribeEvent
	public static void onItemDroped(ItemTossEvent e) {

		EntityItem entityItem = e.getEntityItem();

		e.setCanceled(true);

		if (entityItem.getItem().getItem() == SB_Items.NATRIUM_CHUNK) {
			dropNatriumChunk(e.getPlayer(), entityItem);
		} else {
			e.setCanceled(false);
		}

	}

	private static void dropNatriumChunk(EntityPlayer player, EntityItem entityItem) {

		if (!player.world.isRemote) {

			player.getEntityWorld().spawnEntity(new EntityItemNatriumChunk(entityItem));
		}
	}

}
