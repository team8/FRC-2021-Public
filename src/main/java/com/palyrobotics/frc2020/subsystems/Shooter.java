package com.palyrobotics.frc2020.subsystems;

import static com.palyrobotics.frc2020.config.constants.ShooterConstants.*;
import static com.palyrobotics.frc2020.util.Util.clamp;

import java.util.Map;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.Timer;

public class Shooter extends SubsystemBase {

	public enum ShooterState {
		IDLE, VISION, CUSTOM
	}

	public enum HoodState {
		LOW, MEDIUM, HIGH
	}

	private static Shooter sInstance = new Shooter();

	private Limelight mLimelight = Limelight.getInstance();
	private ShooterConfig mConfig = Configs.get(ShooterConfig.class);
	private boolean mVelocityChanged = false; // If the target velocity has been changed (for rumble)
	private double mTargetVelocity; // Current target velocity (stored here for rumble)
	private Timer mTimer = new Timer();

	/* Outputs */
	private ControllerOutput mFlywheelOutput = new ControllerOutput(); // Flywheel
	private boolean mBlockingOutput, mHoodOutput; // Two solenoids to control the hood
	private boolean mRumbleOutput; // XBox controller rumble

	/* States */
	private Double mTargetDistance;

	private Shooter() {
		mTimer.start();
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		mTargetDistance = getTargetDistance();
		ShooterState wantedShooterState = commands.getShooterWantedState();
		switch (wantedShooterState) {
			case VISION:
				if (mTargetDistance != null) {
					updateVision(state);
					break;
				}
			case IDLE:
				updateIdle(state);
				break;
			case CUSTOM:
				updateCustom(state, commands.getShooterWantedVelocity(), commands.getShooterWantedHoodState());
				break;
		}

		updateRumble(state);
	}

	/**
	 * Updates all of the outputs for the IDLE shooting state
	 */
	private void updateIdle(@ReadOnly RobotState state) {
		mFlywheelOutput.setTargetVelocity(0, mConfig.shooterVelocityGains);
		mVelocityChanged = false;
	}

	/**
	 * Updates all of the outputs for the IDLE shooting state
	 */
	private void updateVision(@ReadOnly RobotState state) {
		// Updating the hood
		Map.Entry<Double, HoodState> floorEntry = kTargetDistanceToHoodState.floorEntry(mTargetDistance);
		Map.Entry<Double, HoodState> ceilingEntry = kTargetDistanceToHoodState.ceilingEntry(mTargetDistance);
		Map.Entry<Double, HoodState> closestEntry;
		if (ceilingEntry == null) {
			closestEntry = floorEntry;
		} else if (floorEntry == null) {
			closestEntry = ceilingEntry;
		} else {
			closestEntry = mTargetDistance - floorEntry.getKey() < ceilingEntry
					.getKey() - mTargetDistance ? floorEntry : ceilingEntry;
		}
		HoodState hoodState = closestEntry.getValue();
		translateHoodState(state, hoodState);

		// Updating the velocity
		double targetFlywheelVelocity;

		targetFlywheelVelocity = kTargetDistanceToVelocity.get(hoodState).getInterpolated(mTargetDistance);

		mTargetVelocity = clamp(targetFlywheelVelocity, kMinVelocity, kMaxVelocity);
		mFlywheelOutput.setTargetVelocity(mTargetVelocity, mConfig.shooterVelocityGains);
	}

	/**
	 * Updates all of the outputs for the VISION shooting state
	 */
	private void updateCustom(@ReadOnly RobotState state, double shooterVelocity, HoodState hoodState) {
		mFlywheelOutput.setTargetVelocity(shooterVelocity, mConfig.shooterVelocityGains);
		translateHoodState(state, hoodState);

		mTargetVelocity = shooterVelocity;
	}

	private void updateRumble(@ReadOnly RobotState state) {
		if (Math.abs(state.shooterFlywheelVelocity - mTargetVelocity) <= mConfig.rumbleError) {
			mRumbleOutput = true;

			if (mVelocityChanged) {
				mTimer.reset();
				mVelocityChanged = false;
			} else if (mTimer.hasElapsed(mConfig.rumbleTimeSeconds)) {
				mRumbleOutput = false;
			}
		} else {
			mRumbleOutput = false;
		}
	}

	/* Translates HoodState to outputs */

	private void translateHoodState(@ReadOnly RobotState state, HoodState hoodState) {
		switch (hoodState) {
			case LOW:
				setHoodLow(state);
				break;
			case MEDIUM:
				setHoodMedium(state);
				break;
			case HIGH:
				setHoodHigh(state);
				break;
		}
	}

	private void setHoodLow(@ReadOnly RobotState state) {
		/*
		When we are down, always make sure our locking piston is set to unblocking.
		This is how other states tell if we are down instead of just resting on top
		of the block, since the hood piston is retracted in case those two cases,
		meaning its extension state can't be used to determine physical position.
		Because of how this is done, there is no need for checks here.
		*/
		mBlockingOutput = false;
		mHoodOutput = state.shooterBlockingSolenoidState;
	}

	private void setHoodMedium(@ReadOnly RobotState state) {
		if (state.shooterBlockingSolenoidState) {
			/* Hood is already at the top or middle state. If we were in low state,
			* BlockingSolenoidState would be false. */
			mHoodOutput = false;
			mBlockingOutput = true;
		} else {
			/* We are at the low hood position. Because state.blockingSolenoidState
			
			is false,
			* We can only be low. If we are transitioning from low to high, it will still
			* be false. */
			mHoodOutput = true;
			/*
			Unblock until the hood reaches the top, then block.
			This moves to the first if condition and moves the
			hood down to rest on top of the blocking piston. This
			will prevent errors from occurring during the transition phase.
			*/
			mBlockingOutput = state.shooterHoodSolenoidState;
		}
	}

	private void setHoodHigh(@ReadOnly RobotState state) {
		/*
		This assumes that we will never be in the state where
		our blocking piston is extended and our hood is pushing
		upwards against it.
		*/
		mHoodOutput = true;
		if (state.shooterBlockingSolenoidState) {
			/* If we are in middle state continue locking */
			mBlockingOutput = true;
		} else {
			/* We are in bottom state, wait until hood is fully extended to lock */
			mBlockingOutput = state.shooterHoodSolenoidState;
		}
	}

	/**
	 * A utility function to be used inside of the shooter class.
	 *
	 * @return The distance to the target, and an error if it has not been found yet
	 */
	private Double getTargetDistance() {
		if (mLimelight.isTargetFound()) {
			return mLimelight.getEstimatedDistanceInches();
		}
		return null;
	}

	/* Getters */
	public static Shooter getInstance() {
		return sInstance;
	}

	public ControllerOutput getFlywheelOutput() {
		return mFlywheelOutput;
	}

	public boolean getHoodOutput() {
		return mHoodOutput;
	}

	public boolean getBlockingOutput() {
		return mBlockingOutput;
	}

	/**
	 * The rumble will rumble once we are ready to shoot or if their are any errors (Target not found)
	 *
	 * @return If the xbox should rumble
	 */
	public boolean getRumbleOutput() {
		return mRumbleOutput;
	}
}
