package com.sciencebitch.mod;

import com.sciencebitch.mod.handlers.GuiHandler;
import com.sciencebitch.proxy.CommonProxy;

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
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

		ScienceBitch.proxy.initialize();
		RecipeManager.initialize();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}

}
