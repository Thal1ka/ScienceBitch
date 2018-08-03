package com.sciencebitch.util;

import java.util.List;

import com.sciencebitch.interfaces.IEnergyProvider;
import com.sciencebitch.interfaces.IEnergySink;

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

	public static int transferEnergyToBlocks(IEnergyProvider provider, List<IEnergySink> energyReceivers) {

		int energyToGive = Math.min(provider.getEnergyLeft(null), provider.getMaxEnergyOutput());
		final int energyToGiveBefore = energyToGive;

		for (int i = 0; i < energyReceivers.size(); i++) {

			IEnergySink receiver = energyReceivers.get(i);

			int transferAmount = Math.min(energyToGive / (energyReceivers.size() - i), receiver.getCapacityLeft(null));

			transferAmount = receiver.injectEnergy(provider, transferAmount, null);
			energyToGive -= transferAmount;
		}

		int energyTaken = energyToGiveBefore - energyToGive;
		provider.drainEnergy(ENTITY_DUMMY, energyTaken, null);

		return energyTaken;
	}
}
