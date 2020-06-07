package com.palyrobotics.frc2020.config.subsystem;

import com.palyrobotics.frc2020.util.config.SubsystemConfigBase;
import com.palyrobotics.frc2020.util.control.Gains;

public class TurretConfig extends SubsystemConfigBase {

	public double turretAngleSoftStopRange;
	public double poseChangeLookBackSec;
	public Gains turretGains;
	public int visionPnPMedianFilterSize;
}
