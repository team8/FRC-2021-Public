package com.palyrobotics.frc2020.robot;

import java.awt.*;
import java.util.ArrayList;

import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.constants.DriveConstants;
import com.palyrobotics.frc2020.util.Util;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Holds the current physical state of the robot from our sensors.
 */
@SuppressWarnings ("java:S1104")
public class RobotState {

	public enum GamePeriod {
		AUTO, TELEOP, TESTING, DISABLED
	}

	public static final String kLoggerTag = Util.classToJsonName(RobotState.class);

	/* Drive */
	private final DifferentialDriveOdometry driveOdometry = new DifferentialDriveOdometry(new Rotation2d());
	public double driveYawDegrees, driveYawAngularVelocityDegrees;
	public boolean driveIsQuickTurning, driveIsSlowTurning;
	public double driveLeftVelocity, driveRightVelocity, driveLeftPosition, driveRightPosition;
	public Pose2d drivePoseMeters = new Pose2d();
	public double driveVelocityMetersPerSecond;
	public boolean driveIsGyroReady;
	/* Shooter */
	public double shooterFlywheelVelocity;
	public boolean shooterIsReadyToShoot;
	public boolean shooterIsHoodExtended, shooterIsBlockingExtended;
	public boolean shooterHoodIsInTransition;

	/* Intake */
	public boolean intakeExtended, intakeTransitioning, intakeStalled;

	/* Indexer */
	public boolean indexerPos1Blocked, indexerPos4Blocked;
	public double indexerMasterEncPosition, indexerMasterEncVelocity, indexerSlaveEncPosition, indexerSlaveEncVelocity,
			indexerLeftVTalonCurrentDraw, indexerRightVTalonCurrentDraw, indexerSlaveCurrentDraw;

	/* Joystick */
	public boolean joystickRightTriggerPressed;

	/* Spinner */
	public String closestColorString;
	public double closestColorConfidence;
	public Color detectedRGBValues;
	public ColorMatchResult closestColorRGB;

	/* Misc */
	public ArrayList<Float> radii;
	public ArrayList<Point> centers;

	public GamePeriod gamePeriod = GamePeriod.DISABLED;
	public String gameData;
	public double gameTime;

	public void resetOdometry(Pose2d pose) {
		driveOdometry.resetPosition(pose, pose.getRotation());
		drivePoseMeters = driveOdometry.getPoseMeters();
		driveVelocityMetersPerSecond = 0.0;
		Log.info(kLoggerTag, String.format("Odometry reset to: %s", pose));
	}

	public void updateOdometry(double yawDegrees, double leftMeters, double rightMeters) {
		drivePoseMeters = driveOdometry.update(Rotation2d.fromDegrees(yawDegrees), leftMeters, rightMeters);
		ChassisSpeeds speeds = DriveConstants.kKinematics.toChassisSpeeds(new DifferentialDriveWheelSpeeds(driveLeftVelocity, driveRightVelocity));
		driveVelocityMetersPerSecond = Math.sqrt(Math.pow(speeds.vxMetersPerSecond, 2) + Math.pow(speeds.vyMetersPerSecond, 2));
	}
}
