package com.sciencebitch.util;

import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyProvider;
import com.sciencebitch.interfaces.energy.IEnergyReceiver;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyHelper {

	public static final EntityLivingDummy ENTITY_DUMMY = new EntityLivingDummy(null);

	public static int transferEnergy(IEnergyStorage storage, IEnergyReceiver receiver, ItemStack receiverStack) {

		if (!storage.canExtract()) return 0;

		int transferValue = Math.min(receiver.getCapacityLeft(receiverStack), receiver.getMaxEnergyInput());
		transferValue = storage.extractEnergy(transferValue, false);
		receiver.injectEnergy(null, transferValue, receiverStack);

		return transferValue;
	}

	public static int transferEnergy(IEnergyProvider provider, ItemStack providerStack, IEnergyStorage storage) {

		if (!storage.canReceive()) return 0;

		int transferValue = Math.min(provider.getEnergyLeft(providerStack), provider.getMaxEnergyOutput());
		transferValue = storage.receiveEnergy(transferValue, false);
		provider.drainEnergy(ENTITY_DUMMY, transferValue, providerStack);

		return transferValue;
	}

	public static int transferEnergyIntoBlocks(IEnergyStorage provider, List<IEnergyStorage> energyReceivers) {

		int energyToGive = provider.getEnergyStored();
		energyToGive = Math.min(energyToGive, provider.extractEnergy(energyToGive, true));
		int energyToGiveCopy = energyToGive;

		for (int i = 0; i < energyReceivers.size(); i++) {

			IEnergyStorage receiver = energyReceivers.get(i);

			int capacity = receiver.getMaxEnergyStored() - receiver.getEnergyStored();
			int transferAmount = Math.min(energyToGive / energyReceivers.size(), capacity);

			transferAmount = receiver.receiveEnergy(transferAmount, false);
			energyToGive -= transferAmount;
		}

		int energyTransfered = energyToGiveCopy - energyToGive;
		provider.extractEnergy(energyTransfered, false);

		return energyTransfered;
	}
}
