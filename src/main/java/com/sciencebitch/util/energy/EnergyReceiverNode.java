package com.sciencebitch.util.energy;

import java.util.ArrayList;
import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyReceiverNode extends EnergyNode {

	private final IEnergyStorage consumer;
	private int allowedEnergy;
	private int lossToReceiver;

	public EnergyReceiverNode(IEnergyStorage consumer) {
		this.consumer = consumer;
	}

	public void setAllowedEnergy(int allowedEnergy) {
		this.allowedEnergy = Math.max(allowedEnergy - lossToReceiver, 0);
	}

	public IEnergyStorage getConsumer() {
		return this.consumer;
	}

	@Override
	public int getEnergyConsumption() {

		if (energyConsumption >= 0) return energyConsumption;

		energyConsumption = consumer.getMaxEnergyStored() - consumer.getEnergyStored();
		energyConsumption = consumer.receiveEnergy(energyConsumption, true);
		energyConsumption += lossToReceiver;

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
	public float handleLoss(float previousLoss) {
		lossToReceiver = (int) previousLoss;
		return previousLoss - lossToReceiver;
	}

	@Override
	public int submitEnergy() {
		return this.consumer.receiveEnergy(allowedEnergy, false) + lossToReceiver;
	}

	@Override
	public void toString(StringBuilder builder, int level) {

		builder.append("-> ").append(level).append(": Consumer ");
	}
}
