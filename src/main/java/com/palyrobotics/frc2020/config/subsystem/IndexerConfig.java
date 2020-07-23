package com.palyrobotics.frc2020.config.subsystem;

import com.palyrobotics.frc2020.util.config.SubsystemConfigBase;

public class IndexerConfig extends SubsystemConfigBase {

	public int columnStallCurrentLimit, columnFreeCurrentLimit, vTalonCurrentLimit;
	public double rampRate, nativeToInchPosConversion, powercellIndexDistance,
			feedSpeed, reverseFeedSpeed, rightVTalonPo, leftVTalonPo;
	public boolean blockingSolenoidExtended, hopperSolenoidExtended;

}
