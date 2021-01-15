package com.palyrobotics.frc2021.config.subsystem;

import com.palyrobotics.frc2021.util.config.SubsystemConfigBase;
import com.palyrobotics.frc2021.util.control.ProfiledGains;

@SuppressWarnings ("squid:ClassVariableVisibilityCheck")
public class ClimberConfig extends SubsystemConfigBase {

	public double climberTopHeight;
	public double allowablePositionError;
	public double currentDrawWhenClimbing;

	public double raisingArbitraryDemand;
	public ProfiledGains raisingGains;
}
