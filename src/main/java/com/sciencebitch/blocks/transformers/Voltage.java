package com.sciencebitch.blocks.transformers;

public enum Voltage {

	LOW_VOLTAGE(0), MED_VOLTAGE(1), HIGH_VOLTAGE(2);

	private final int voltageLevel;

	Voltage(int voltageLevel) {

		this.voltageLevel = voltageLevel;
	}

	public boolean isGreater(Voltage compare) {

		return this.voltageLevel > compare.voltageLevel;
	}
}
