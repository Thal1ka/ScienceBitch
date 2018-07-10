package com.sciencebitch.mod.handlers;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.blocks.SB_Blocks;
import com.sciencebitch.interfaces.IBlockHandler;
import com.sciencebitch.interfaces.IHasModel;
import com.sciencebitch.interfaces.IItemHandler;
import com.sciencebitch.items.SB_Items;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class RegistryHandler {

	private static final List<IBlockHandler> blockHandlers = new ArrayList<>();
	private static final List<IItemHandler> itemHandlers = new ArrayList<>();

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> e) {

		e.getRegistry().registerAll(SB_Items.ITEMS.toArray(new Item[0]));

		IForgeRegistry<Item> registry = e.getRegistry();

		for (IItemHandler handler : itemHandlers) {
			registry.registerAll(handler.getItemsToRegister().toArray(new Item[0]));
		}
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> e) {

		e.getRegistry().registerAll(SB_Blocks.BLOCKS.toArray(new Block[0]));

		IForgeRegistry<Block> registry = e.getRegistry();

		for (IBlockHandler handler : blockHandlers) {
			registry.registerAll(handler.getBlocksToRegister().toArray(new Block[0]));
		}

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

	public static void registerBlockHandler(IBlockHandler handler) {
		blockHandlers.add(handler);
	}

	public static void registerItemHandler(IItemHandler handler) {
		itemHandlers.add(handler);
	}
}
