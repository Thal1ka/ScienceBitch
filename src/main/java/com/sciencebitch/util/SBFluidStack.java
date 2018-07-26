package com.sciencebitch.util;

import com.sciencebitch.mod.handlers.FluidHandler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class SBFluidStack {

	private int fluidType;
	private int fluidAmount;

	public SBFluidStack(Fluid fluid) {
		this(fluid, 0);
	}

	public SBFluidStack(Fluid fluid, int amount) {

		this(FluidHandler.getId(fluid), amount);
	}

	public SBFluidStack(int fluidType) {
		this(fluidType, 0);
	}

	public SBFluidStack(int fluidType, int amount) {

		this.fluidType = fluidType;
		this.fluidAmount = amount;
	}

	public SBFluidStack(FluidStack stack) {
		this(stack.getFluid(), stack.amount);
	}

	public int getFluidType() {
		return fluidType;
	}

	public void setFluidType(int fluidType) {
		this.fluidType = fluidType;
	}

	public int getFluidAmount() {
		return fluidAmount;
	}

	public void grow(int amount) {
		this.fluidAmount += amount;
	}

	public void shrink(int amount) {
		this.fluidAmount = Math.max(fluidAmount - amount, 0);
	}

	public void saveToNbt(String key, NBTTagCompound nbt) {

		nbt.setInteger(key + ":fluidType", fluidType);
		nbt.setInteger(key + ":fluidAmount", fluidAmount);
	}

	public static SBFluidStack loadFromNbt(String key, NBTTagCompound nbt) {

		if (!nbt.hasKey(key + ":fluidType")) return null;

		int type = nbt.getInteger(key + ":fluidType");
		int amount = nbt.getInteger(key + ":fluidAmount");
		return new SBFluidStack(type, amount);
	}
}
