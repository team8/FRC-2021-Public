package com.palyrobotics.frc2020.subsystems.controllers.drive;

import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.SynchronousPIDF;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.MedianFilter;

public class BallPickupController extends ChezyDriveController {

    private static final String kLoggerTag = Util.classToJsonName(BallPickupController.class);
    public static final int kFilterSize = 3;
    private final Limelight mLimelight = Limelight.getInstance();
    private final SynchronousPIDF mDistancePidController = new SynchronousPIDF();
    private final SynchronousPIDF mAnglePidController = new SynchronousPIDF();
    private VisionConfig mVisionConfig = Configs.get(VisionConfig.class);
    private MedianFilter mTargetYawFilter = new MedianFilter(kFilterSize);
    private MedianFilter mTargetDistanceFilter = new MedianFilter(kFilterSize);
    private int mTargetFoundCount;
    private double mTargetGyroYaw;
    private double mTargetDistance;

    public BallPickupController() {
    }

    @Override
    public void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        if (state.driveIsGyroReady) {
            double gyroYawDegrees = state.driveYawDegrees;
            double gyroYawAngularVelocity = state.driveYawAngularVelocityDegrees;
            //TODO: Need a way of getting current position
            double postition = 0;
            double velocityMetersPerSecond =  state.driveVelocityMetersPerSecond;
            if (mLimelight.isTargetFound()) {
                double visionYawToTargetDegrees = mLimelight.getYawToTarget();
                double visionDistanceToTarget = mLimelight.getEstimatedDistanceInches(); //TODO: may make a new method or convert this to meters
                mTargetGyroYaw = mTargetYawFilter.calculate(gyroYawDegrees - visionYawToTargetDegrees);
                mTargetDistance = mTargetDistanceFilter.calculate(postition - visionDistanceToTarget);
                mTargetFoundCount++;
            } else {
                mTargetFoundCount = 0;
            }
            if (mTargetFoundCount >= kFilterSize) {
                setOutput(calculateDistance(mTargetDistance, postition, velocityMetersPerSecond), calculateAngle(mTargetGyroYaw, gyroYawDegrees, -gyroYawAngularVelocity));
                return;
            }
        } else {
            if (mLimelight.isTargetFound()) {
                //TODO: change estimated distance part
                setOutput(calculateDistance(0, mLimelight.getEstimatedDistanceInches(), null), calculateAngle(0.0, mLimelight.getYawToTarget(), null));
            }
            mTargetFoundCount = 0;
        }
        super.updateSignal(commands, state);
    }

    private double calculateAngle(double targetDegrees, double degrees, Double degreesDerivative) {
        var preciseGains = mVisionConfig.preciseGains;
        mAnglePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
        mAnglePidController.setSetpoint(targetDegrees);
        double percentOutput = degreesDerivative == null ? mAnglePidController.calculate(degrees) : mAnglePidController.calculate(degrees, degreesDerivative);
        double turnGainS = mConfig.turnGainsS;
        if (Math.abs(mAnglePidController.getError()) < mVisionConfig.acceptableYawError) turnGainS *= 0.2;
        return percentOutput + Math.signum(percentOutput) * turnGainS;
    }

    //TODO: change more of the stuff to distance related
    private double calculateDistance(double targetDistance, double currentDistance, Double accelerationDerivative) {
        var preciseGains = mVisionConfig.preciseGains;
        mDistancePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
        mDistancePidController.setSetpoint(targetDistance);
        double percentOutput = accelerationDerivative == null ? mDistancePidController.calculate(currentDistance) : mDistancePidController.calculate(currentDistance, accelerationDerivative);
        double turnGainS = mConfig.turnGainsS;
        if (Math.abs(mDistancePidController.getError()) < mVisionConfig.acceptableYawError) turnGainS *= 0.2;
        return percentOutput + Math.signum(percentOutput) * turnGainS;
    }

    private void setOutput(double distanceOut, double angleOut) {
        mOutputs.leftOutput.setPercentOutput(-distanceOut*angleOut);
        mOutputs.rightOutput.setPercentOutput(distanceOut*angleOut);
    }
}
