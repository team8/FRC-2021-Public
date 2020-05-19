package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.wpilibj.MedianFilter;

public class Shooter extends SubsystemBase {

    public enum FlywheelState {
        IDLE, CUSTOM_VELOCITY, VISION_VELOCITY
    }

    private static Shooter sInstance = new Shooter();
    private ControllerOutput mFlywheelOutput = new ControllerOutput();
    private boolean mBlockingOutput;
    private Limelight mLimelight = Limelight.getInstance();
    private MedianFilter mDistanceFilter = new MedianFilter(15);

    private Shooter() {}

    public static Shooter getInstance() {
        return sInstance;
    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        switch (commands.shooterWantedState) {
            case VISION_VELOCITY:
                mBlockingOutput = false;
                break;
            case CUSTOM_VELOCITY:
                mBlockingOutput = false;
                break;
            case IDLE:
                mBlockingOutput = true;
                break;
        }
    }

    private Double getTargetDistance() {
        return mLimelight.getEstimatedDistanceInches();
    }

    public ControllerOutput getFlywheelOutput() {
        return mFlywheelOutput;
    }


}
