package com.sciencebitch.interfaces.energy;

import java.util.List;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyConnector {

	List<IEnergyConnector> getConnectedCables();

	List<IEnergyStorage> getConnectedConsumers();

	void addCurrentThroughConnector(int amount);
}
