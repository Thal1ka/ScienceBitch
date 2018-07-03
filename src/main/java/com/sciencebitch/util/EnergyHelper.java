package com.sciencebitch.util;

import net.minecraft.item.ItemStack;

public class EnergyHelper {

	public static final EntityLivingDummy ENTITY_DUMMY = new EntityLivingDummy(null);

	public static int transferEnergy(IEnergyProvider provider, IEnergySink sink) {

		return transferEnergy(provider, null, sink, null);
	}

	public static int transferEnergy(IEnergyProvider provider, ItemStack providerStack, IEnergySink sink) {

		return transferEnergy(provider, providerStack, sink, null);
	}

	public static int transferEnergy(IEnergyProvider provider, IEnergySink sink, ItemStack sinkStack) {

		return transferEnergy(provider, null, sink, sinkStack);
	}

	public static int transferEnergy(IEnergyProvider provider, ItemStack providerStack, IEnergySink sink, ItemStack sinkStack) {

		int maxAmountProvider, maxAmountSink;

		maxAmountProvider = Math.min(provider.getMaxEnergyOutput(), provider.getEnergyLeft(providerStack));
		maxAmountSink = Math.min(sink.getMaxEnergyInput(), sink.getCapacityLeft(sinkStack));

		int transferAmount = Math.min(maxAmountProvider, maxAmountSink);

		provider.drainEnergy(ENTITY_DUMMY, transferAmount, providerStack);
		sink.injectEnergy(provider, transferAmount, sinkStack);

		return transferAmount;
	}

}
