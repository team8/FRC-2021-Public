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

    /* Outputs */
    private ControllerOutput mFlywheelOutput = new ControllerOutput(); // Flywheel
    private Boolean mBlockingOutput, mHoodOutput; // Two solenoids to control the hood
    private Boolean mRumbleOutput; // XBox controller rumble
    private Boolean mIsReadyToShoot; // Whether the hood and fly wheel are close enough to their wanted states

    /* States */
    private HoodState mHoodState =

    private Shooter() {

    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {

    }

    private void updateStates(@ReadOnly RobotState state) {
        
    }

    private void updateIdle(ControllerOutput controllerOutput, Boolean blockingOutput, Boolean hoodOutput) {

    }

    /**
     * Returns the best velocity to shoot the ball. This should only be called if ShooterState is VISION. IDLE and CUSTOM
     * are simple
     * @param hoodState
     * @param targetDistance
     * @return
     */
    private void updateFlywheelVelocity(HoodState hoodState, double targetDistance) {
    }

    private HoodState updateHood(double targetDistance) {
        return null;
    }

    /**
     * A utility function to be used inside of the shooter class.
     * @return The distance to the target, and an error if it has not been found yet
     */
    private double getTargetDistance() {
        if (mLimelight.isTargetFound()) {
            return mLimelight.getEstimatedDistanceInches();
        }
        throw new IllegalStateException("Limelight target not found");
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
