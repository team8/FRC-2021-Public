package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.wpilibj.MedianFilter;

import java.util.Map;

import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToHoodState;
import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToVelocity;
import static com.palyrobotics.frc2020.util.Util.clamp;

public class Shooter extends SubsystemBase {

    public enum ShooterState {
        IDLE, CUSTOM_VELOCITY, VISION_VELOCITY
    }

    public enum HoodState {
        LOW, MIDDLE, HIGH
    }

    private static Shooter sInstance = new Shooter();
    private ShooterConfig mConfig = Configs.get(ShooterConfig.class);
    private ControllerOutput mFlywheelOutput = new ControllerOutput();
    private boolean mBlockingOutput, mHoodOutput;
    private Limelight mLimelight = Limelight.getInstance();
    private MedianFilter mDistanceFilter = new MedianFilter(15);

    private Shooter() {}

    public static Shooter getInstance() {
        return sInstance;
    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
         mFlywheelOutput.setTargetVelocity(getTargetFlywheelVelocity(commands, getHoodState()), mConfig.flywheelGains);
    }

    private double getTargetFlywheelVelocity(@ReadOnly Commands commands, HoodState hoodState) {
        double targetFlywheelVelocity = 0;
        switch (commands.getShooterWantedState()) {
            case VISION_VELOCITY:
                Double targetDistance = getTargetDistance();
                if (targetDistance != null) {
                    targetFlywheelVelocity = kTargetDistanceToVelocity.get(hoodState).getInterpolated(targetDistance);
                    break;
                }
            case CUSTOM_VELOCITY:
                targetFlywheelVelocity = commands.getShooterWantedCustomFlywheelVelocity();
                break;
        }
        return clamp(targetFlywheelVelocity, 0, mConfig.maxVelocity);
    }

    private Double getTargetDistance() {
        if (mLimelight.isTargetFound()) {
            return mDistanceFilter.calculate(mLimelight.getEstimatedDistanceInches());
        }
        return null;
    }

    private HoodState getHoodState() {
        Double targetDistance = getTargetDistance();
        if (targetDistance == null) {
            return HoodState.HIGH;
        }
        Map.Entry<Double, HoodState> floorEntry = kTargetDistanceToHoodState.floorEntry(targetDistance),
                ceilingEntry = kTargetDistanceToHoodState.ceilingEntry(targetDistance);
        if (floorEntry == null) {
            return ceilingEntry.getValue();
        }
        if (ceilingEntry == null) {
            return floorEntry.getValue();
        }
        return (targetDistance - floorEntry.getKey()) < (ceilingEntry.getKey() - targetDistance) ? floorEntry.getValue() : ceilingEntry.getValue();
    }


    public ControllerOutput getFlywheelOutput() {
        return mFlywheelOutput;
    }

    public boolean getBlockingOutput() {
        return mBlockingOutput;
    }

    public boolean getHoodOutput() {
        return mHoodOutput;
    }

}
