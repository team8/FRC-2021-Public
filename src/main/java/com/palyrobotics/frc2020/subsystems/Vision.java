package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.control.LimelightControlMode;
import com.palyrobotics.frc2020.util.control.LimelightControlMode.*;

/**
 * No longer a wrapper around the Limelight network tables, now it is a full on subsystem.
 */
public class Vision extends SubsystemBase {

	// To keep the Limelight class at a decent size, the enums will not be stored in here like other subsystems.

	private static final Vision sInstance = new Vision();

	private LedMode wantedLEDMode;
	private CamMode wantedCamMode;
	private int wantedPipeline;

	private Vision() {
	}

	public static Vision getInstance() {
		return sInstance;
	}

	@Override
	public void update(Commands commands, RobotState state) {
		if (commands.visionWanted) {
			wantedCamMode = LimelightControlMode.CamMode.VISION;
			wantedLEDMode = LimelightControlMode.LedMode.FORCE_ON;
		} else {
			wantedCamMode = LimelightControlMode.CamMode.DRIVER;
			wantedLEDMode = LimelightControlMode.LedMode.FORCE_OFF;
		}

		wantedPipeline = commands.visionWantedPipeline;
	}

	public int getPipeline() {
		return wantedPipeline;
	}

	public CamMode getCamMode() {
		return wantedCamMode;
	}

	public LedMode getLEDMode() {
		return wantedLEDMode;
	}
}
