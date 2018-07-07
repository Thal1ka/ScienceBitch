package com.sciencebitch.mod.handlers;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.blocks.machines.MachineCreator;
import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.items.SB_Items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> e) {

		e.getRegistry().registerAll(SB_Items.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> e) {

		e.getRegistry().registerAll(SB_Blocks.BLOCKS.toArray(new Block[0]));
		e.getRegistry().registerAll(MachineCreator.instance().getBlocksToRegister().toArray(new Block[0]));
		TileEntityHandler.registerTileEntities();
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {

		for (Item item : SB_Items.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel) item).registerModel();
			}
		}

		for (Block block : SB_Blocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModel();
			}
		}
	}
}
