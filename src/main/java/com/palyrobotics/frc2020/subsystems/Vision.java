package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.control.Limelight;
import com.palyrobotics.frc2020.util.control.LimelightControlMode;
import com.palyrobotics.frc2020.util.control.LimelightControlMode.*;

/**
 * No longer a wrapper around the Limelight network tables, now it is a full on subsystem.
 */
public class Vision extends SubsystemBase {

	// To keep the Limelight class at a decent size, the enums will not be stored in here like other subsystems.

	private static final Vision sInstance = new Vision();

	private Limelight mLimelight = new Limelight();

	private Vision() {
	}

	public static Vision getInstance() {
		return sInstance;
	}

	@Override
	public void update(Commands commands, RobotState state) {
		if (commands.visionWanted) {
			mLimelight.setCamMode(LimelightControlMode.CamMode.VISION);
			mLimelight.setLEDMode(LimelightControlMode.LedMode.FORCE_ON);
		} else {
			mLimelight.setCamMode(LimelightControlMode.CamMode.DRIVER);
			mLimelight.setLEDMode(LimelightControlMode.LedMode.FORCE_OFF);
		}

		mLimelight.setPipeline(commands.visionWantedPipeline);
	}

	public double getVisionTargetInches() {
		return mLimelight.getEstimatedDistanceInches();
	}

	public int getPipeline() {
		return mLimelight.getPipeline();
	}

	public CamMode getCamMode() {
		return mLimelight.getCamMode();
	}
}