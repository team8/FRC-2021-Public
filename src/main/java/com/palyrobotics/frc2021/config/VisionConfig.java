package com.palyrobotics.frc2021.config;

import com.palyrobotics.frc2021.util.config.ConfigBase;
import com.palyrobotics.frc2021.util.control.Gains;
import com.palyrobotics.frc2021.util.control.ProfiledGains;

@SuppressWarnings ("java:S1104")
public class VisionConfig extends ConfigBase {

	public ProfiledGains profiledGains;
	public Gains preciseGains;
	public double acceptableYawError, alignSwitchYawAngleMin;

	public Gains oneTimesZoomGains, twoTimesZoomGains;
}
