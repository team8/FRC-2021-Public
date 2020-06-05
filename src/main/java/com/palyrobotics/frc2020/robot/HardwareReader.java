package com.palyrobotics.frc2020.robot;

import java.util.Set;

import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.sensors.PigeonIMU.PigeonState;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.RobotConfig;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.robot.HardwareAdapter.*;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.subsystems.Turret;
import com.palyrobotics.frc2020.util.Pair;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.Falcon;
import com.palyrobotics.frc2020.util.control.Spark;
import com.palyrobotics.frc2020.util.control.Talon;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;
import com.palyrobotics.frc2020.vision.Limelight;
import com.revrobotics.CANSparkMax.FaultID;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

public class HardwareReader {

	private static final String kLoggerTag = Util.classToJsonName(HardwareReader.class);
	private static final int kYawIndex = 0, kYawAngularVelocityIndex = 2;
	private final RobotConfig mRobotConfig = Configs.get(RobotConfig.class);
	private final DriveConfig mDriveConfig = Configs.get(DriveConfig.class);
	private Limelight mLimelight = Limelight.getInstance();

	private final double[] mGyroAngles = new double[3], mGyroAngularVelocities = new double[3];

	public HardwareReader() {
	}

	/**
	 * Takes all of the sensor data from the hardware, and unwraps it into the current
	 * {@link RobotState}.
	 */
	void updateState(Set<SubsystemBase> enabledSubsystems, RobotState state) {
		readGameAndFieldState(state);
		Robot.mDebugger.addPoint("readGameAndFieldState");
		if (enabledSubsystems.contains(Drive.getInstance())) readDriveState(state);
		if (enabledSubsystems.contains(Shooter.getInstance())) readShooterState(state);
		if (enabledSubsystems.contains(Turret.getInstance())) readTurretState(state);
		Robot.mDebugger.addPoint("Drive");
	}

	private void readGameAndFieldState(RobotState state) {
		state.gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	private void readDriveState(RobotState state) {
		var hardware = DriveHardware.getInstance();
		/* Gyro */
		state.driveIsGyroReady = hardware.gyro.getState() == PigeonState.Ready;
		if (state.driveIsGyroReady) {
			hardware.gyro.getYawPitchRoll(mGyroAngles);
			state.driveYawDegrees = mGyroAngles[kYawIndex];
			hardware.gyro.getRawGyro(mGyroAngularVelocities);
			state.driveYawAngularVelocityDegrees = mGyroAngularVelocities[kYawAngularVelocityIndex];
		}
		/* Falcons */
		state.driveLeftVelocity = hardware.leftMasterFalcon.getConvertedVelocity();
		state.driveRightVelocity = hardware.rightMasterFalcon.getConvertedVelocity();
		state.driveLeftPosition = hardware.leftMasterFalcon.getConvertedPosition();
		state.driveRightPosition = hardware.rightMasterFalcon.getConvertedPosition();
//		LiveGraph.add("x", state.drivePoseMeters.getTranslation().getX());
//		LiveGraph.add("y", state.drivePoseMeters.getTranslation().getY());
//		LiveGraph.add("leftPosition", state.driveLeftPosition);
//		LiveGraph.add("rightPosition", state.driveRightPosition);
		/* Odometry */
		state.updateOdometry(state.driveYawDegrees, state.driveLeftPosition, state.driveRightPosition);
		state.inShootingQuadrant = state.drivePoseMeters.getTranslation().getX() > mDriveConfig.xBoundShootingQuadrant && state.drivePoseMeters.getTranslation().getY() < mDriveConfig.yBoundShootingQuadrant;
		if (state.inShootingQuadrant) {
			Pose2d drivePoseRotationBounded = new Pose2d(state.drivePoseMeters.getTranslation(), Rotation2d.fromDegrees(Util.boundAngleNeg180to180Degrees(state.drivePoseMeters.getRotation().getDegrees())));
			state.pastPoses.put(Timer.getFPGATimestamp(), drivePoseRotationBounded);
		}
//		LiveGraph.add("driveLeftPosition", state.driveLeftPosition);

		LiveGraph.add("driveLeftVelocity", state.driveLeftVelocity);
//		LiveGraph.add("driveRightPosition", state.driveRightPosition);
		LiveGraph.add("driveRightVelocity", state.driveRightVelocity);
//		LiveGraph.add("driveYaw", state.driveYawDegrees);
//		LiveGraph.add("driveRightPercentOutput", hardware.rightMasterFalcon.getMotorOutputPercent());
//		LiveGraph.add("driveLeftPercentOutput", hardware.leftMasterFalcon.getMotorOutputPercent());
		hardware.falcons.forEach(this::checkFalconFaults);
	}

	private void readShooterState(RobotState state) {
		var hardware = ShooterHardware.getInstance();
		state.shooterBlockingIsExtended = hardware.blockingSolenoid.isExtended();
		state.shooterHoodPistonIsExtended = hardware.hoodPiston.isExtended();
		state.shooterHoodTransitioning = hardware.blockingSolenoid.isInTransition() || hardware.hoodPiston.isInTransition();
		state.shooterFlywheelVelocity = hardware.masterEncoder.getVelocity();
		checkSparkFaults(hardware.masterSpark);
		checkSparkFaults(hardware.slaveSpark);
	}

	private void readTurretState(RobotState state) {
		var talon = TurretHardware.getInstance().talon;
		state.turretYawDegrees = talon.getConvertedPosition();
	}

	private void checkSparkFaults(Spark spark) {
		if (mRobotConfig.checkFaults) {
			boolean wasAnyFault = false;
			for (var value : FaultID.values()) {
				boolean isFaulted = spark.getStickyFault(value);
				if (isFaulted) {
					Log.error(kLoggerTag, String.format("Spark %d fault: %s", spark.getDeviceId(), value));
					wasAnyFault = true;
				}
			}
			if (wasAnyFault) {
				spark.clearFaults();
			}
		}
	}

	private void checkTalonFaults(Talon talon) {
		if (mRobotConfig.checkFaults) {
			var faults = new StickyFaults();
			talon.getStickyFaults(faults);
			if (faults.hasAnyFault()) {
				Log.error(kLoggerTag, String.format("%s faults: %s", talon.getName(), faults));
				talon.clearStickyFaults();
			}
		}
	}

	private void checkFalconFaults(Falcon falcon) {
		if (mRobotConfig.checkFaults) {
			var faults = new StickyFaults();
			falcon.getStickyFaults(faults);
			if (faults.hasAnyFault()) {
				Log.error(kLoggerTag, String.format("%s faults: %s", falcon.getName(), faults));
				falcon.clearStickyFaults();
			}
		}
	}
}
