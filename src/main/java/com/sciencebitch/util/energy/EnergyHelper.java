package com.sciencebitch.util.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.sciencebitch.interfaces.energy.IEnergyConnector;
import com.sciencebitch.interfaces.energy.IEnergyProvider;
import com.sciencebitch.interfaces.energy.IEnergyReceiver;
import com.sciencebitch.util.EntityLivingDummy;

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
		List<EnergyReceiverNode> leaves = buildConnectionTreeAndGetLeaves(sourceNode, provider);

		int energyToGive = provider.getEnergyStored();
		energyToGive = provider.extractEnergy(energyToGive, true);

		distributeEnergy(leaves, energyToGive);

		int submittedEnergy = sourceNode.submitEnergy();
		provider.extractEnergy(submittedEnergy, false);
	}

	private static List<EnergyReceiverNode> buildConnectionTreeAndGetLeaves(EnergyNode parent, IEnergyStorage provider) {

		List<EnergyReceiverNode> leaves = new ArrayList<>();

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
					EnergyConnectorNode node = new EnergyConnectorNode(connectedCable);

					parent.addChild(node);
					queue.offer(node);
				}
			}

			List<IEnergyStorage> connectedConsumers = parent.getConnectedConsumers();

			for (IEnergyStorage connectedConsumer : connectedConsumers) {
				if (!closedConsumers.contains(connectedConsumer)) {

					closedConsumers.add(connectedConsumer);
					EnergyReceiverNode node = new EnergyReceiverNode(connectedConsumer);

					// Nodes without actual consumption should not be processed
					if (node.getEnergyConsumption() > 0) {
						parent.addChild(node);
						leaves.add(node);
					}
				}
			}
		}

		return leaves;
	}

	private static void distributeEnergy(List<EnergyReceiverNode> leaves, int energyToGive) {

		Collections.sort(leaves);

		for (int i = 0; i < leaves.size(); i++) {

			EnergyReceiverNode leave = leaves.get(i);
			int leaveEnergy = Math.min(leave.getEnergyConsumption(), energyToGive / (leaves.size() - i));
			leave.setAllowedEnergy(leaveEnergy);

			energyToGive -= leaveEnergy;
		}
	}
}
