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
        IDLE, VISION, CUSTOM
    }

    public enum HoodState {
        LOW, MEDIUM, HIGH
    }

    private static Shooter sInstance = new Shooter();

    private Limelight mLimelight = Limelight.getInstance();

    private ControllerOutput mFlywheelOutput = new ControllerOutput(); // Flywheel
    private boolean mBlockingOutput, mHoodOutput; // Two solenoids to control the hood
    private boolean mRumbleOutput; // XBox controller rumble

    private Shooter() {

    }

    @Override
    public void update(Commands commands, RobotState state) {

    }

    public ControllerOutput updateFlywheelVelocity(Commands commands, RobotState state, HoodState hoodState, double targetDistance) {
        return null;
    }

    public HoodState updateHood(Commands commands, RobotState state, double targetDistance) {
        return null;
    }

    private double getTargetDistance(Commands commands, RobotState robotState) {
        return -1;
    }

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

    public boolean getRumbleOutput() {
        return mRumbleOutput;
    }

    public boolean isReadyToShoot() {
        return mIsReadyToShoot;
    }
}
