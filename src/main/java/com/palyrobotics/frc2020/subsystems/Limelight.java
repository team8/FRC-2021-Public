package com.palyrobotics.frc2020.subsystems;

import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
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

	private Limelight() {
	}

	public static Limelight getInstance() {
		return sInstance;
	}

	@Override
	public void update(Commands commands, RobotState state) {

	}
}
