package com.palyrobotics.frc2020.config.constants;

import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

public class TurretConstants {
	public static final InterpolatingDoubleTreeMap turretAngleMultiplier = new InterpolatingDoubleTreeMap();
	public static final double turretAngleHardStopRange = 180;
	public static final double turretAngleSoftStopRange = 160;
	public static final double drivetrain2TurretX = 5;
	public static final double drivetrain2TurretY = 5;
	private static final int inclinationRange = 10;

	static {
		turretAngleMultiplier.put(0.0, 0.0);
		turretAngleMultiplier.put((turretAngleHardStopRange - turretAngleSoftStopRange)/2.0, 0.0);
		turretAngleMultiplier.put((turretAngleHardStopRange - turretAngleSoftStopRange)/2.0 + inclinationRange, 1.0);
		turretAngleMultiplier.put((turretAngleHardStopRange + turretAngleSoftStopRange)/2.0 - inclinationRange, 1.0);
		turretAngleMultiplier.put((turretAngleHardStopRange + turretAngleSoftStopRange)/2.0, 0.0);
		turretAngleMultiplier.put(turretAngleHardStopRange, 0.0);
	}
}
