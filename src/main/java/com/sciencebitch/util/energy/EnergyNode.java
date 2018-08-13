package com.sciencebitch.util.energy;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyNode implements Comparable<EnergyNode> {

	protected final List<EnergyConnectorNode> connectorChildren = new ArrayList<>();
	protected final List<EnergyReceiverNode> consumerChildren = new ArrayList<>();

	private EnergyNode parent;

	protected int energyConsumption = -1;

	public abstract int getEnergyConsumption();

	public void addChild(EnergyConnectorNode child) {
		connectorChildren.add(child);
		child.setParent(this);
	}

	public void addChild(EnergyReceiverNode child) {
		consumerChildren.add(child);
		child.setParent(this);
	}

	public void setParent(EnergyNode parent) {
		this.parent = parent;
	}

	public abstract List<IEnergyConnector> getConnectedCables();

	public abstract List<IEnergyStorage> getConnectedConsumers();

	public abstract int submitEnergy();

	public abstract void toString(StringBuilder builder, int level);

	@Override
	public int compareTo(EnergyNode compareNode) {
		return this.getEnergyConsumption() - compareNode.getEnergyConsumption();
	}
}
