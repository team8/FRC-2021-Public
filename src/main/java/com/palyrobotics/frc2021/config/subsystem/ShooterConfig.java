package com.palyrobotics.frc2021.config.subsystem;

import com.palyrobotics.frc2021.util.config.SubsystemConfigBase;
import com.palyrobotics.frc2021.util.control.Gains;

@SuppressWarnings ("squid:ClassVariableVisibilityCheck")
public class ShooterConfig extends SubsystemConfigBase {

	public Gains velocityGains;
	public double maxVelocity, velocityTolerance, acceptableDriveVelocity;
	public double rumbleDurationSeconds;
	public double hoodSwitchDistanceThreshold;
	public double noTargetSpinUpVelocity;
	// TODO: remove
	public double customVelocity;
}
