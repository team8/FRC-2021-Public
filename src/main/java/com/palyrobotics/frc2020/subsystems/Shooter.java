package com.palyrobotics.frc2020.subsystems;

import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToHoodState;
import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToVelocity;
import static com.palyrobotics.frc2020.util.Util.clamp;

import java.util.Map;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.MedianFilter;

import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

public class Shooter extends SubsystemBase {

    public enum  ShooterState {
        IDLE, VISION, TARGETING, CUSTOM
    }

    public enum HoodState {
        LOW, MEDIUM, HIGH
    }

    private static Shooter sInstance = new Shooter();

    private Limelight mLimelight = Limelight.getInstance();
    private ShooterConfig mConfig = Configs.get(ShooterConfig.class);

    /* Outputs */
    private ControllerOutput mFlywheelOutput = new ControllerOutput(); // Flywheel
    private boolean mBlockingOutput, mHoodOutput; // Two solenoids to control the hood
    private boolean mRumbleOutput; // XBox controller rumble
    private boolean mIsReadyToShoot; // Whether the hood and fly wheel are close enough to their wanted states

    /* States */
    private HoodState mHoodState;
    private boolean mBlockingSolenoidState;
    private boolean mHoodSolenoidState;
    private double mShooterVelocity;
    private Double mTargetDistance;

    private Shooter() {

    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        updateStates(state);

        ShooterState wantedShooterState = commands.getWantedShooterState();
        switch (wantedShooterState) {
            case VISION:
                if (mTargetDistance != null) {
                    updateVision();
                    break;
                }
            case TARGETING:
                if (mTargetDistance != null) {
                    updateTargeting();
                    break;
                }
            case IDLE:
                updateIdle();
                break;
            case CUSTOM:
                updateCustom();
                break;
        }
    }

    /**
     * Updates the local states to those found in RobotState
     * @param state RobotState
     */
    private void updateStates(@ReadOnly RobotState state) {
        // Not updating mHoodState here because that will be what it is currently supposed to be
        mBlockingSolenoidState = state.blockingSolenoidState;
        mHoodSolenoidState = state.hoodSolenoidState;
        mShooterVelocity = state.shooterVelocity;
        mTargetDistance = getTargetDistance();
    }

    /**
     * Updates all of the outputs for the IDLE shooting state
     */
    private void updateIdle() {

    }

    /**
     * Updates all of the outputs for the IDLE shooting state
     */
    private void updateVision() {

    }

    /**
     * Updates all of the outputs for the IDLE shooting state
     */
    private void updateCustom(double shooterVelocity, HoodState hoodState) {
        mFlywheelOutput.setTargetVelocity(shooterVelocity, mConfig.shooterGains);
    }

    /**
     * Updates all of the outputs for the IDLE shooting state
     */
    private void updateTargeting() {

    }

    /* Translates HoodState to outputs */

    private void setHoodLow() {
        /*
        When we are down, always make sure our locking piston is set to unblocking.
        This is how other states tell if we are down instead of just resting on top
        of the block, since the hood piston is retracted in case those two cases,
        meaning its extension state can't be used to determine physical position.
        */
        mBlockingOutput = false;
        mHoodOutput = mBlockingSolenoidState;
    }

    private void setHoodMedium() {
        if (mBlockingSolenoidState) {
            /* Hood is already at the top or middle state */
            mHoodOutput = false;
            mBlockingOutput = true;
        } else {
            /* We are at the low hood position. */
            mHoodOutput = true;
					/*
					Unblock until the hood reaches the top, then block.
					This moves to the first if condition and moves the
					hood down to rest on top of the blocking piston.
					*/
            mBlockingOutput = mHoodSolenoidState;
        }
    }

    private void setHoodHigh() {
        /*
        This assumes that we will never be in the state where
        our blocking piston is extended and our hood is pushing
        upwards against it.
        */
        mHoodOutput = true;
        if (mBlockingSolenoidState) {
            // If we are in middle state continue locking
            mBlockingOutput = true;
        } else {
            // We are in bottom state, wait until hood is fully extended to lock
            mBlockingOutput = mHoodSolenoidState;
        }
    }

    /**
     * A utility function to be used inside of the shooter class.
     * @return The distance to the target, and an error if it has not been found yet
     */
    private Double getTargetDistance() {
        if (mLimelight.isTargetFound()) {
            return mLimelight.getEstimatedDistanceInches();
        }
        return null;
    }

    /* Getters */
    public Shooter getInstance() {
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
     * @return If the xbox should rumble
     */
    public boolean getRumbleOutput() {
        return mRumbleOutput;
    }

    public boolean isReadyToShoot() {
        return mIsReadyToShoot;
    }
}
