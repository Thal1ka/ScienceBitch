package com.sciencebitch.util.energy;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyReceiverNode extends EnergyNode {

	private final IEnergyStorage consumer;
	private int allowedEnergy;

	public EnergyReceiverNode(IEnergyStorage consumer) {
		this.consumer = consumer;
	}

	public void setAllowedEnergy(int allowedEnergy) {
		this.allowedEnergy = allowedEnergy;
	}

	public IEnergyStorage getConsumer() {
		return this.consumer;
	}

	@Override
	public int getEnergyConsumption() {

		if (energyConsumption >= 0) return energyConsumption;

		energyConsumption = consumer.getMaxEnergyStored() - consumer.getEnergyStored();
		energyConsumption = consumer.receiveEnergy(energyConsumption, true);

		return energyConsumption;
	}

	@Override
	public List<IEnergyConnector> getConnectedCables() {
		return new ArrayList<>();
	}

	@Override
	public List<IEnergyStorage> getConnectedConsumers() {
		return new ArrayList<>();
	}

	@Override
	public int submitEnergy() {
		return this.consumer.receiveEnergy(allowedEnergy, false);
	}

	@Override
	public void toString(StringBuilder builder, int level) {

		builder.append("-> ").append(level).append(": Consumer ");
	}
}
