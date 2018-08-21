package com.sciencebitch.util.energy;

import java.util.Collections;
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
	public float handleLoss(float previousLoss) {

		int consumerChildrenAmount = getConsumerChildrenAmount();

		if (consumerChildrenAmount == 0) return previousLoss;
		previousLoss += connector.getLoss();

		float spareLoss = 0;
		int amountRemoteConsumers = 0;

		for (EnergyConnectorNode connectorNode : connectorChildren) {

			int remoteConsumers = connectorNode.getConsumerChildrenAmount();
			amountRemoteConsumers += remoteConsumers;
			spareLoss += connectorNode.handleLoss(previousLoss * remoteConsumers / consumerChildrenAmount);
		}

		if (amountRemoteConsumers == 0) {

			previousLoss = spareLoss + previousLoss * consumerChildren.size() / consumerChildrenAmount;
			if (previousLoss < 1) return previousLoss;

			int prevLoss = (int) previousLoss;
			int lossPerConsumer = 0;
			int consumersToGiveLoss = consumerChildrenAmount;

			for (; consumersToGiveLoss > 0; consumersToGiveLoss--) {
				lossPerConsumer = prevLoss / consumersToGiveLoss;
				if (lossPerConsumer > 0) {
					break;
				}
			}

			Collections.shuffle(consumerChildren);

			for (EnergyReceiverNode receiverNode : consumerChildren) {
				receiverNode.handleLoss(lossPerConsumer);
			}

			return previousLoss - lossPerConsumer * consumersToGiveLoss;
		}

		return spareLoss;
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
