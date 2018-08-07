package com.sciencebitch.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyNode {

	private final List<EnergyNode> children = new ArrayList<>();
	private final IEnergyStorage energyReceiver;
	private final IEnergyConnector connector;

	int energyConsumption = -1;

	public EnergyNode(@Nullable IEnergyStorage energyReceiver, @Nullable IEnergyConnector connector) {

		this.energyReceiver = energyReceiver;
		this.connector = connector;
	}

	public int getEnergyConsumption() {

		if (energyConsumption >= 0) return energyConsumption;

		energyConsumption = 0;

		if (energyReceiver != null) {

			energyConsumption = energyReceiver.getMaxEnergyStored() - energyReceiver.getEnergyStored();
			energyConsumption = energyReceiver.extractEnergy(energyConsumption, true);
			return energyConsumption;
		}

		for (EnergyNode child : children) {
			energyConsumption += child.getEnergyConsumption();
		}

		return energyConsumption;
	}

	public void addChild(EnergyNode child) {
		children.add(child);
	}

	public List<IEnergyConnector> getConnectedCables() {

		if (connector == null) return new ArrayList<>();

		return connector.getConnectedCables();
	}

	public List<IEnergyStorage> getConnectedConsumers() {

		if (connector == null) return new ArrayList<>();

		return connector.getConnectedConsumers();
	}

	public int submitEnergy(int energyAmount) {

		int energyToGive = energyAmount;
		int energyToGiveCopy = energyToGive;

		Collections.sort(children, (n1, n2) -> compareChildren(n1, n2));

		for (int i = 0; i < children.size(); i++) {

			EnergyNode child = children.get(i);
			int transferAmount = Math.min(energyToGive / children.size(), child.getEnergyConsumption());

			transferAmount = child.submitEnergy(transferAmount);
			energyToGive -= transferAmount;
		}

		int energyTransfered = energyToGiveCopy - energyToGive;
		return energyTransfered;
	}

	private int compareChildren(EnergyNode n1, EnergyNode n2) {
		return n1.getEnergyConsumption() - n2.getEnergyConsumption();
	}

	public void toString(StringBuilder builder, int i) {

		builder.append(" -> ").append(i).append(": ");
		if (connector != null) {
			builder.append("Cable");
		} else if (energyReceiver != null) {
			builder.append("Storage");
		} else {
			builder.append("Provider");
		}

		for (EnergyNode child : children) {
			child.toString(builder, i + 1);
		}
	}
}
