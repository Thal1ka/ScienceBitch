package com.sciencebitch.mod.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHandler {

	private static int nextId = 0;

	private static final Map<Fluid, Integer> ids = new HashMap<>();
	private static final Map<Integer, Fluid> fluids = new TreeMap<>();

	public static final int WATER = registerFluid(FluidRegistry.WATER);
	public static final int LAVA = registerFluid(FluidRegistry.LAVA);

	private static int registerFluid(Fluid fluid) {

		int id = nextId;

		ids.put(fluid, nextId);
		fluids.put(nextId, fluid);
		nextId++;

		return id;
	}

	public static Fluid getFluid(int id) {

		return fluids.get(id);
	}

	public static int getId(Fluid fluid) {
		return ids.get(fluid);
	}
}
