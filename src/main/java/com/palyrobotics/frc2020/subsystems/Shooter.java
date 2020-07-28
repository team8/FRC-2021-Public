package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
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

    private ControllerOutput mFlyWheelOutput = new ControllerOutput(); // Flywheel
    private boolean mBlockingOutput, mHoodOutput; // Two solenoids to control the hood
    private boolean mRumbleOutput; // XBox controller rumble

    private Shooter() {

    }

    @Override
    public void update(Commands commands, RobotState state) {

    }
}
