package com.sciencebitch.util.energy;

import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyConnectorNode extends EnergyNode {

	private final IEnergyConnector connector;

	public EnergyConnectorNode(IEnergyConnector connector) {

		this.connector = connector;
	}

	@Override
	public int getEnergyConsumption() {

		if (energyConsumption >= 0) return energyConsumption;

		energyConsumption = 0;

		for (EnergyNode child : connectorChildren) {
			energyConsumption += child.getEnergyConsumption();
		}

		for (EnergyNode child : consumerChildren) {
			energyConsumption += child.getEnergyConsumption();
		}

		return energyConsumption;
	}

	private IEnergyConnector getConnector() {
		return this.connector;
	}

	@Override
	public List<IEnergyConnector> getConnectedCables() {

		return connector.getConnectedCables();
	}

	@Override
	public List<IEnergyStorage> getConnectedConsumers() {

		return connector.getConnectedConsumers();
	}

	@Override
	public int submitEnergy() {

		int energySubmitted = 0;

		for (EnergyConnectorNode connectorNode : connectorChildren) {
			energySubmitted = connectorNode.submitEnergy();
		}

		for (EnergyReceiverNode consumerNode : consumerChildren) {
			energySubmitted += consumerNode.submitEnergy();
		}

		connector.addCurrentThroughConnector(energySubmitted);

		return energySubmitted;
	}

	public boolean hasReceivers() {
		return !consumerChildren.isEmpty();
	}

	@Override
	public void toString(StringBuilder builder, int level) {

		builder.append(" -> ").append(level).append(": Connector ");

		for (EnergyReceiverNode consumer : consumerChildren) {
			consumer.toString(builder, level + 1);
		}

		for (EnergyConnectorNode connector : connectorChildren) {
			connector.toString(builder, level + 1);
		}
	}

}
