package com.palyrobotics.frc2020.config.subsystem;

import com.palyrobotics.frc2020.util.config.SubsystemConfigBase;
import com.palyrobotics.frc2020.util.control.Gains;

public class IndexerConfig extends SubsystemConfigBase {

	public int columnStallCurrentLimit, columnFreeCurrentLimit, vTalonCurrentLimit;
	public double rampRate, nativeToInchPosConversion, powercellIndexDistance,
			feedSpeed, reverseFeedSpeed, rightVTalonPo, leftVTalonPo;
	public boolean blockingSolenoidExtended, hopperSolenoidExtended;
	public Gains positionGains;

}
