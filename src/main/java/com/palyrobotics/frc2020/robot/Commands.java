package com.palyrobotics.frc2020.robot;

import java.util.ArrayList;
import java.util.List;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.subsystems.Lighting;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.subsystems.Shooter;
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

	/* Indexer */
	public Indexer.ColumnState indexerColumnWantedState;
	public Indexer.VSingulatorState indexerVSingulatorWantedState;

	/* Intake */
	private Intake.State intakeWantedState;
	private double intakeWantedPo;

	/* Vision */
	public int visionWantedPipeline;
	public boolean visionWanted;
	/* Miscellaneous */
	public boolean wantedCompression;
	/* Lighting */
	public ArrayList<Lighting.State> lightingWantedStates;
	/* Shooter */
	private Shooter.ShooterState wantedShooterState;
	private Shooter.HoodState wantedShooterHoodState; // Only needed for custom
	private double wantedShooterVelocity; // Only needed for custom

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

	/* Shooter */

	/**
	 * Self documenting code down here, sets the shooter to IDLE, this means it will set the flywheel
	 * velocity to 0, and keep the same hood state.
	 */
	public void setIdleShooterState() {
		this.wantedShooterState = Shooter.ShooterState.IDLE;
	}

	/**
	 * Also self documenting: Sets the shooter state to VISION, this means it will try and set the hood
	 * state to the best of its knowledge using interpolation from the "carefully" chosen data points we
	 * have tested.
	 */
	public void setVisionShooterState() {
		this.wantedShooterState = Shooter.ShooterState.VISION;
	}

	/**
	 * This should not be used in the match, it only exists to allow us to test velocities and hood
	 * states for data to be used in interpolation.
	 *
	 * @param velocity  The velocity to be tested
	 * @param hoodState The hood state to be tested
	 */
	public void setCustomShooterState(double velocity, Shooter.HoodState hoodState) {
		this.wantedShooterState = Shooter.ShooterState.CUSTOM;
		this.wantedShooterVelocity = velocity;
		this.wantedShooterHoodState = hoodState;
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

	public void setIntakeRunning(double intakeWantedPo) {
		intakeWantedState = Intake.State.RUNNING;
		this.intakeWantedPo = intakeWantedPo;
	}

	public void setIntakeStowed() {
		intakeWantedState = Intake.State.STOWED;
	}

	public Intake.State getIntakeWantedState() {
		return intakeWantedState;
	}

	public double getIntakeWantedPo() {
		return intakeWantedPo;
	}

	public double getWantedShooterVelocity() {
		return wantedShooterVelocity;
	}

	public Shooter.HoodState getWantedShooterHoodState() {
		return wantedShooterHoodState;
	}

	public Shooter.ShooterState getWantedShooterState() {
		return wantedShooterState;
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
