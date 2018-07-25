package com.sciencebitch.mod.handlers;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.IContainerProvider;
import com.sciencebitch.interfaces.IGuiProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	private List<IContainerProvider> containerProviders = new ArrayList<>();
	private List<IGuiProvider> guiProviders = new ArrayList<>();

	public int registerGui(IContainerProvider container, IGuiProvider gui) {

		int id = containerProviders.size();
		containerProviders.add(container);
		guiProviders.add(gui);

		return id;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (ID < 0 || ID >= containerProviders.size()) throw new IllegalArgumentException("Unregistered GUI ID: " + ID);

		return containerProviders.get(ID).getContainer(player.inventory, world.getTileEntity(new BlockPos(x, y, z)));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (ID < 0 || ID >= guiProviders.size()) throw new IllegalArgumentException("Unregistered GUI ID: " + ID);

		return guiProviders.get(ID).getGui(player.inventory, world.getTileEntity(new BlockPos(x, y, z)));
	}

}
