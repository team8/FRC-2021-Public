package com.palyrobotics.frc2020.config.subsystem;

import com.palyrobotics.frc2020.util.config.ConfigBase;
import com.palyrobotics.frc2020.util.control.Gains;

public class ShooterConfig extends ConfigBase {

	public Gains shooterVelocityGains;
	public double rumbleError;
	public double rumbleTimeSeconds;
	public double hoodSolenoid;
	public double blockingSolenoid;
}
