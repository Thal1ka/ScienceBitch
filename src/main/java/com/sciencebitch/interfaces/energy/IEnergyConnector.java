package com.sciencebitch.interfaces.energy;

import java.util.List;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyConnector {

	List<IEnergyConnector> getConnectedCables();

	List<IEnergyStorage> getConnectedConsumers();

	void addCurrentThroughConnector(int amount);

	/**
	 * Wether the cables masters the energy transmition of the given storage
	 *
	 * @param storage
	 *            The storage
	 * @return {@code True} if if is the master of the connection
	 */
	boolean isMaster(IEnergyStorage storage);
}
