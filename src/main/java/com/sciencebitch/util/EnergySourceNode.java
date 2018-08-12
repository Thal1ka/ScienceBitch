package com.sciencebitch.util;

import java.util.List;

import com.sciencebitch.interfaces.energy.IEnergyConnector;

public class EnergySourceNode extends EnergyNode {

	private final List<IEnergyConnector> connectedCables;

	public EnergySourceNode(List<IEnergyConnector> connectedCables) {
		super(null, null);

		this.connectedCables = connectedCables;
	}

	@Override
	public List<IEnergyConnector> getConnectedCables() {
		return connectedCables;
	}
}
