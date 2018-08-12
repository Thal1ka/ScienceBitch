package com.sciencebitch.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

		Set<IEnergyConnector> closedConnectors = new HashSet<>();
		Set<IEnergyStorage> closedConsumers = new HashSet<>();

		EnergySourceNode sourceNode = new EnergySourceNode(connectedWires);
		buildConnectionTree(sourceNode, provider);

		// StringBuilder builder = new StringBuilder();
		// src.toString(builder, 0);
		// builder.append("\n\n");
		// System.out.println(builder);

		int energyConsumption = sourceNode.getEnergyConsumption();

		energyConsumption = provider.extractEnergy(energyConsumption, true);
		energyConsumption = sourceNode.submitEnergy(energyConsumption);

		provider.extractEnergy(energyConsumption, false);
	}

	private static void buildConnectionTree(EnergyNode parent, IEnergyStorage provider) {

		Set<IEnergyConnector> closedCables = new HashSet<>();
		Set<IEnergyStorage> closedConsumers = new HashSet<>();

		// To avoid self supply if provider can receive and provide
		closedConsumers.add(provider);

		Queue<EnergyNode> queue = new LinkedList<>();
		queue.offer(parent);

		while (!queue.isEmpty()) {

			parent = queue.poll();

			List<IEnergyConnector> connectedCables = parent.getConnectedCables();

			for (IEnergyConnector connectedCable : connectedCables) {
				if (!closedCables.contains(connectedCable)) {
					closedCables.add(connectedCable);
					EnergyNode node = new EnergyNode(null, connectedCable);
					parent.addChild(node);
					queue.offer(node);
				}
			}

			List<IEnergyStorage> connectedConsumers = parent.getConnectedConsumers();

			for (IEnergyStorage connectedConsumer : connectedConsumers) {
				if (!closedConsumers.contains(connectedConsumer)) {
					closedConsumers.add(connectedConsumer);
					EnergyNode node = new EnergyNode(connectedConsumer, null);
					parent.addChild(node);
				}
			}
		}
	}
}
