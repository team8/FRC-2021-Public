package com.palyrobotics.frc2020.subsystems.controllers.drive;

import static com.palyrobotics.frc2020.util.Util.clamp;

import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.SynchronousPIDF;
import com.palyrobotics.frc2020.vision.Limelight;

public class BallPickupController extends ChezyDriveController {

	private static final String kLoggerTag = Util.classToJsonName(BallPickupController.class);
	public static final int kFilterSize = 3;
	private final Limelight mLimelight = Limelight.getInstance();
	private final SynchronousPIDF mDistancePidController = new SynchronousPIDF();
	private final SynchronousPIDF mAnglePidController = new SynchronousPIDF();
	private VisionConfig mVisionConfig = Configs.get(VisionConfig.class);
	private int mTargetFoundCount;
	private double mTargetGyroYaw = 0;
	private double mTargetDistance = 0;

	public BallPickupController() {
	}

	@Override
	public void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		if (state.driveIsGyroReady) { //Do I need this if statement?
			double currentAngle = 0; //Probably state.balls.center when odroid is implemented
			double gyroYawAngularVelocity = state.driveYawAngularVelocityDegrees; //This will probably work due to the way PID works, but may change
			double currentPosition = 0; //Probably state.balls.radius later when odroid is implemented
			double velocityMetersPerSecond = state.driveVelocityMetersPerSecond;
			if (mLimelight.isTargetFound()) {
				mTargetFoundCount++;
			} else {
				mTargetFoundCount = 0;
			}
			if (mTargetFoundCount >= kFilterSize) {
				setOutput(calculateDistance(mTargetDistance, currentPosition, velocityMetersPerSecond), calculateAngle(mTargetGyroYaw, currentAngle, -gyroYawAngularVelocity));
				return;
			}
		} else { //if I don't need the if statement above delete this
			if (mLimelight.isTargetFound()) {
				double currentPosition = 0; //Probably state.balls.radius later if even needed
				setOutput(calculateDistance(0, currentPosition, null), calculateAngle(0.0, mLimelight.getYawToTarget(), null));
			}
			mTargetFoundCount = 0;
		}
		super.updateSignal(commands, state);
	}

	private double calculateAngle(double targetDegrees, double degrees, Double degreesDerivative) {
		var preciseGains = mVisionConfig.preciseGains; //Create new config class for Automatic Ball Pickup?
		mAnglePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
		mAnglePidController.setSetpoint(targetDegrees);
		/* If deleting the if statement for gyro ready, then there will be no null so this will be
		return mDistancePidController.calculate(degrees, degreesDerivative);
		*/
		return degreesDerivative == null ? mAnglePidController.calculate(degrees) : mAnglePidController.calculate(degrees, degreesDerivative);
	}

	private double calculateDistance(double targetDistance, double currentDistance, Double accelerationDerivative) {
		var preciseGains = mVisionConfig.preciseGains; //Create new config class for Automatic Ball Pickup?
		mDistancePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
		mDistancePidController.setSetpoint(targetDistance);
		/* If deleting the if statement for gyro ready, then there will be no null so this will be
		return mDistancePidController.calculate(currentDistance, accelerationDerivative);
		*/
		return accelerationDerivative == null ? mDistancePidController.calculate(currentDistance) : mDistancePidController.calculate(currentDistance, accelerationDerivative);
	}

	private void setOutput(double distanceOut, double angleOut) {
		//Check if these are the correct min and max values
		double rightOutput = clamp(distanceOut + angleOut, -1, 1);
		double leftOutput = clamp(distanceOut - angleOut, -1, 1);
		mOutputs.leftOutput.setPercentOutput(leftOutput);
		mOutputs.rightOutput.setPercentOutput(rightOutput);
	}
}
