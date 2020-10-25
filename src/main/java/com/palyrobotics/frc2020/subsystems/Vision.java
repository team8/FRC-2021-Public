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

	/**
	 * A getter that should only be used in one place,
	 *
	 * @return
	 */
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

	/**
	 * Gives the pipeline that we want to switch to. The pipeline is stored as an int from 0 to 9 TODO:
	 * double check this, I know there are 9 I just need to know if its 0 indexed
	 *
	 * @return The pipeline we want to use
	 */
	public int getPipeline() {
		return wantedPipeline;
	}

	/**
	 * Gives the cam mode {@link CamMode} we want to switch to
	 *
	 * @return
	 */
	public CamMode getCamMode() {
		return wantedCamMode;
	}

	/**
	 * Gives the LED mode {@link LedMode} which we want to use
	 *
	 * @return
	 */
	public LedMode getLEDMode() {
		return wantedLEDMode;
	}
}
