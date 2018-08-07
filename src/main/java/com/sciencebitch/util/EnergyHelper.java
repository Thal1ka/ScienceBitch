package com.sciencebitch.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sciencebitch.interfaces.energy.IEnergyConnector;
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

	public static void transferEnergyThroughConnectors(IEnergyStorage provider, List<IEnergyConnector> connectedWires) {

		Set<IEnergyConnector> closedList = new HashSet<>();
		Set<IEnergyStorage> detectedConsumers = new HashSet<>();

		EnergyNode src = new EnergyNode(null, null);

		for (IEnergyConnector connector : connectedWires) {

			EnergyNode child = new EnergyNode(null, connector);

			src.addChild(child);
			closedList.add(connector);
			connector.addUsedStorage(provider);

			buildConnectionTree(child, closedList, detectedConsumers);
		}

		StringBuilder builder = new StringBuilder();
		src.toString(builder, 0);
		builder.append("\n\n");

		System.out.println(builder);

		int energyConsumption = src.getEnergyConsumption();
		energyConsumption = provider.extractEnergy(energyConsumption, true);

		energyConsumption = src.submitEnergy(energyConsumption);
		provider.extractEnergy(energyConsumption, false);
	}

	private static void buildConnectionTree(EnergyNode parent, Set<IEnergyConnector> closedList, Set<IEnergyStorage> detectedConsumers) {

		List<IEnergyConnector> connectors = parent.getConnectedCables();
		System.out.println("ConSize:" + connectors.size());

		for (IEnergyConnector connector : connectors) {
			if (!closedList.contains(connector)) {
				closedList.add(connector);
				EnergyNode node = new EnergyNode(null, connector);
				parent.addChild(node);
				buildConnectionTree(node, closedList, detectedConsumers);
			}
		}

		List<IEnergyStorage> consumers = parent.getConnectedConsumers();
		System.out.println("RecSize: " + consumers.size());

		for (IEnergyStorage consumer : consumers) {
			if (!detectedConsumers.contains(consumer)) {
				detectedConsumers.add(consumer);
				parent.addChild(new EnergyNode(consumer, null));
			}
		}
	}
}
