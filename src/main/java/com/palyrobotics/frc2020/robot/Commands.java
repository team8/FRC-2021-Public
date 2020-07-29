package com.palyrobotics.frc2020.robot;

import java.util.ArrayList;
import java.util.List;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.subsystems.Drive;

import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.subsystems.Turret;
import com.palyrobotics.frc2020.util.control.DriveOutputs;

import com.palyrobotics.frc2020.vision.Limelight;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.util.control.DriveOutputs;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

/**
 * Commands represent what we want the robot to be doing.
 */
@SuppressWarnings ("java:S1104")
public class Commands {

	/* Routines */
	public List<RoutineBase> routinesWanted = new ArrayList<>();
	public boolean shouldClearCurrentRoutines;
	/* Drive */
	private Drive.State driveWantedState;
	/* Shooter Commands */
	private Shooter.ShooterState shooterWantedState;
	private Shooter.HoodState hoodWantedState;
	private double shooterWantedFlywheelVelocity;
	/* Turret Commands */
	private Turret.TurretState turretWantedState;
	private double turretWantedAngle; //[0, TurretConstants.turretAngleHardStopRange]

	// Teleop
	private double driveWantedThrottle, driveWantedWheel;
	private boolean driveWantsQuickTurn, driveWantsSlowTurn, driveWantedSlowTurnLeft, driveWantsBrake;
	// Signal
	private DriveOutputs driveWantedSignal;
	// Path Following
	private Trajectory driveWantedTrajectory;
	public Pose2d driveWantedOdometryPose;
	// Turning
	private double driveWantedYawDegrees;
	/* Vision */
	public int visionWantedPipeline;
	public boolean visionWanted;
	/* Miscellaneous */
	public boolean wantedCompression;

	// Intake
	private Intake.State intakeWantedState;

	public void addWantedRoutines(RoutineBase... wantedRoutines) {
		for (RoutineBase wantedRoutine : wantedRoutines) {
			addWantedRoutine(wantedRoutine);
		}
	}

	public void addWantedRoutine(RoutineBase wantedRoutine) {
		routinesWanted.add(wantedRoutine);
	}

	/* Drive */
	public void setDriveOutputs(DriveOutputs outputs) {
		driveWantedState = Drive.State.OUTPUTS;
		driveWantedSignal = outputs;
	}

	public void setDriveFollowPath(Trajectory trajectory) {
		driveWantedState = Drive.State.FOLLOW_PATH;
		driveWantedTrajectory = trajectory;
	}

	public void setDriveVisionAlign(int visionPipeline) {
		driveWantedState = Drive.State.VISION_ALIGN;
		visionWantedPipeline = visionPipeline;
		visionWanted = true;
	}

	public void setDriveTeleop() {
		setDriveTeleop(0.0, 0.0, false, false, false);
	}

	public void setDriveTeleop(double throttle, double wheel, boolean wantsQuickTurn, boolean wantsSlowTurn, boolean wantsBrake) {
		driveWantedState = Drive.State.TELEOP;
		driveWantedThrottle = throttle;
		driveWantedWheel = wheel;
		driveWantsQuickTurn = wantsQuickTurn;
		driveWantsSlowTurn = wantsSlowTurn;
		driveWantsBrake = wantsBrake;
	}

	public void setDriveNeutral() {
		driveWantedState = Drive.State.NEUTRAL;
	}

	public void setDriveYaw(double yawDegrees) {
		driveWantedState = Drive.State.TURN;
		driveWantedYawDegrees = yawDegrees;
	}

	public void setDriveSlowTurnLeft(boolean wantsSlowTurnLeft) {
		driveWantedSlowTurnLeft = wantsSlowTurnLeft;
	}

	public Drive.State getDriveWantedState() {
		return driveWantedState;
	}

	public boolean getDriveWantsQuickTurn() {
		return driveWantsQuickTurn;
	}

	public boolean getDriveWantsSlowTurn() {
		return driveWantsSlowTurn;
	}

	public boolean getDriveWantedSlowTurnLeft() {
		return driveWantedSlowTurnLeft;
	}

	public double getDriveWantedThrottle() {
		return driveWantedThrottle;
	}

	public double getDriveWantedWheel() {
		return driveWantedWheel;
	}

	public boolean getDriveWantsBreak() {
		return driveWantsBrake;
	}

	public Trajectory getDriveWantedTrajectory() {
		return driveWantedTrajectory;
	}

	public double getDriveWantedYawDegrees() {
		return driveWantedYawDegrees;
	}

	public DriveOutputs getDriveWantedSignal() {
		return driveWantedSignal;
	}

	public void setShooterWantedCustomFlywheelVelocity(double flywheelWantedVelocity) {
		this.shooterWantedFlywheelVelocity = flywheelWantedVelocity;
	}

	public void setShooterWantedCustomFlywheelVelocity(double flywheelWantedVelocity, Shooter.HoodState hoodWantedState) {
		this.hoodWantedState = hoodWantedState;
		this.shooterWantedFlywheelVelocity = flywheelWantedVelocity;
	}

	public void setShooterVisionAssisted(int pipelineWanted) {
		visionWantedPipeline = pipelineWanted;
		shooterWantedState = Shooter.ShooterState.VISION_VELOCITY;
	}

	public void setShooterIdle() {
		shooterWantedState = Shooter.ShooterState.IDLE;
	}

	public double getShooterWantedCustomFlywheelVelocity() {
		return shooterWantedFlywheelVelocity;
	}

	public Shooter.ShooterState getShooterWantedState() {
		return shooterWantedState;
	}

	public Shooter.HoodState getHoodWantedState() {
		return hoodWantedState;
	}

	public void setTurretCustomAngle(double turretWantedAngle) {
		turretWantedState = Turret.TurretState.CUSTOM_ANGLE_SETPOINT;
		this.turretWantedAngle = turretWantedAngle;
	}

	public void setTurretVisionAlign() {
		turretWantedState = Turret.TurretState.TARGET_ALIGN;
		visionWantedPipeline = Limelight.kOneTimesZoomPipelineId;
		visionWanted = true;
	}

	public void setTurretIdle() {
		turretWantedState = Turret.TurretState.IDLE;
	}

	public Turret.TurretState getTurretWantedState() {
		return turretWantedState;
	}

	public double getTurretWantedAngle() {
		return turretWantedAngle;
	}
	public Intake.State getIntakeWantedState() {
		return intakeWantedState;
	}

	public void setIntakeWantedState(Intake.State state) {
		intakeWantedState = state;
	}

	@Override
	public String toString() {
		var log = new StringBuilder();
		log.append("Wanted routines: ");
		for (RoutineBase routine : routinesWanted) {
			log.append(routine).append(" ");
		}
		return log.append("\n").toString();
	}

}
