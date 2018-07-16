package com.sciencebitch.mod;

import com.sciencebitch.creativeTabs.SB_CreativeTabs;
import com.sciencebitch.gui.SB_GUIs;
import com.sciencebitch.proxy.CommonProxy;
import com.sciencebitch.recipes.RecipeManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = ScienceBitch.MODID, name = ScienceBitch.MODNAME, version = ScienceBitch.MODVERSION)
public class ScienceBitch {

	public static final String MODID = "sciencebitch";
	public static final String MODNAME = "Science Bitch!";
	public static final String MODVERSION = "1.0";

	public static final String CLIENT_PROXY_CLASS = "com.sciencebitch.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "com.sciencebitch.proxy.CommonProxy";

	@Instance
	public static ScienceBitch instance;

	@SidedProxy(clientSide = ScienceBitch.CLIENT_PROXY_CLASS, serverSide = ScienceBitch.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		// MachineCreator.initialize();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

		ScienceBitch.proxy.initialize();
		RecipeManager.initialize();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, SB_GUIs.guiHandler);
		SB_CreativeTabs.initializeTabIcons();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}

}
