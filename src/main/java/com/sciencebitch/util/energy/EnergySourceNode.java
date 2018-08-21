package com.sciencebitch.util.energy;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergySourceNode extends EnergyNode {

	private final List<IEnergyConnector> connectedCables;

	public EnergySourceNode(List<IEnergyConnector> connectedCables) {

		this.connectedCables = connectedCables;
	}

	@Override
	public int getEnergyConsumption() {

		if (energyConsumption >= 0) return energyConsumption;

		for (EnergyConnectorNode connectorNode : connectorChildren) {

			energyConsumption += connectorNode.getEnergyConsumption();
		}

		return energyConsumption;
	}

	@Override
	public List<IEnergyConnector> getConnectedCables() {
		return connectedCables;
	}

	@Override
	public List<IEnergyStorage> getConnectedConsumers() {
		return new ArrayList<>();
	}

	@Override
	public float handleLoss(float previousLoss) {

		for (EnergyConnectorNode connectorNode : connectorChildren) {
			connectorNode.handleLoss(previousLoss); // previousLoss should be 0 at this point
		}

		return 0F;
	}

	@Override
	public int submitEnergy() {

		int energySubmitted = 0;

		for (EnergyConnectorNode connectorNode : connectorChildren) {

			energySubmitted += connectorNode.submitEnergy();
		}

		return energySubmitted;
	}

	@Override
	public void toString(StringBuilder builder, int level) {

		builder.append("-> ").append(level).append(": Provider ");

		for (EnergyReceiverNode consumer : consumerChildren) {
			consumer.toString(builder, level + 1);
		}

		for (EnergyConnectorNode connector : connectorChildren) {
			connector.toString(builder, level + 1);
		}

		builder.append("\n\n");
	}
}
