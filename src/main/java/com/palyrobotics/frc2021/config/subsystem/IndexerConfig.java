package com.palyrobotics.frc2021.config.subsystem;

import com.palyrobotics.frc2021.util.config.SubsystemConfigBase;
import com.palyrobotics.frc2021.util.control.Gains;

public class IndexerConfig extends SubsystemConfigBase {

	public int columnStallCurrentLimit, columnFreeCurrentLimit, vTalonCurrentLimit;
	public double rampRate, masterSparkIndexDistance, slaveSparkIndexDistance, masterSparkUnIndexDistance, slaveSparkUnIndexDistance,
			masterSparkReverseFeedPo, slaveSparkReverseFeedPo, rightVTalonPo, leftVTalonPo, rightVTalonSlowerPo, indexFinishedMinThreshold,
			unIndexFinishedMinThreshold, maximumIndexerColumnPo, indexControllerTimeoutSec;
	public boolean blockingSolenoidExtended, hopperSolenoidExtended;
	public Gains masterSparkPositionGains, masterSparkVelocityGains, slaveSparkPositionGains, slaveSparkVelocityGains;

}
