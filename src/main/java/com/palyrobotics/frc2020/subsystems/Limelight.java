package com.palyrobotics.frc2020.subsystems;

import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.LimelightControlMode;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.LimelightControlMode.*;

import com.palyrobotics.frc2020.util.control.ControllerOutput;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;

/**
 * Wrapper around the Limelight's network tables
 */
public class Limelight extends SubsystemBase {

	// To keep the Limelight class at a decent size, the enums will not be stored in here like other subsystems.

	private static final Limelight sInstance = new Limelight();
	private static final NetworkTableInstance sNetworkTableInstance = NetworkTableInstance.getDefault();

	private final NetworkTable mTable;

	private Limelight() {
		mTable = sNetworkTableInstance.getTable("limelight");
	}

	public static Limelight getInstance() {
		return sInstance;
	}

	@Override
	public void update(Commands commands, RobotState state) {
		if (commands.visionWanted) {
			setCamMode(LimelightControlMode.CamMode.VISION);
			setLEDMode(LimelightControlMode.LedMode.FORCE_ON);
		} else {
			setCamMode(LimelightControlMode.CamMode.DRIVER);
			setLEDMode(LimelightControlMode.LedMode.FORCE_OFF);
		}

		setPipeline(commands.visionWantedPipeline);
	}

	private void setPipeline(int pipeline) {
		if (pipeline < 0) {
			throw new IllegalArgumentException("Pipeline can not be less than zero");
		} else if (pipeline > 9) {
			throw new IllegalArgumentException("Pipeline can not be greater than nine");
		}
		mTable.getEntry("pipeline").setValue(pipeline);
	}

	private void setCamMode(CamMode camMode) {
		mTable.getEntry("camMode").setValue(camMode.getValue());
	}

	public void setLEDMode(LedMode ledMode) {
		NetworkTableEntry entry = mTable.getEntry("ledMode");
		if (Double.compare(entry.getDouble(0), ledMode.getValue()) != 0) {
			entry.setValue(ledMode.getValue());
			sNetworkTableInstance.flush();
		}
	}
}
