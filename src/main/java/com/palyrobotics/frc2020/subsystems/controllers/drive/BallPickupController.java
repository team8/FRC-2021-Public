package com.palyrobotics.frc2020.subsystems.controllers.drive;

import static com.palyrobotics.frc2020.util.Util.clamp;

import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.SynchronousPIDF;
import com.palyrobotics.frc2020.vision.Limelight;

public class BallPickupController extends ChezyDriveController {

	private static final String kLoggerTag = Util.classToJsonName(BallPickupController.class);
	private final Limelight mLimelight = Limelight.getInstance();
	private final SynchronousPIDF mDistancePidController = new SynchronousPIDF();
	private final SynchronousPIDF mAnglePidController = new SynchronousPIDF();
	private DriveConfig mDriveConfig = Configs.get(DriveConfig.class);

	public BallPickupController() {
	}

	@Override
	public void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		if (state.driveIsGyroReady) {
			double currentAngle = state.balls.get(0).getCenter().getX();
			double gyroYawAngularVelocity = state.driveYawAngularVelocityDegrees; //This will likely work due to the way PID works, but may change
			double currentPosition = state.balls.get(0).getRadius();
			double velocityMetersPerSecond = state.driveVelocityMetersPerSecond; //This will likely work due to the way PID works, but may change
			if (state.balls.size() >= 1) {
				setOutput(calculateDistance(currentPosition, velocityMetersPerSecond), calculateAngle(currentAngle, -gyroYawAngularVelocity));
				return;
			}
		} else {
			if (mLimelight.isTargetFound()) {
				setOutput(calculateDistance(state.balls.get(0).getRadius(), null), calculateAngle(state.balls.get(0).getCenter().getX(), null));
			}
		}
		super.updateSignal(commands, state);
	}

	private double calculateAngle(double degrees, Double degreesDerivative) {
		var preciseGains = mDriveConfig.ballPickupTurnGains;
		mAnglePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
		mAnglePidController.setSetpoint(0);
		return degreesDerivative == null ? mAnglePidController.calculate(degrees) : mAnglePidController.calculate(degrees, degreesDerivative);
	}

	private double calculateDistance(double currentDistance, Double accelerationDerivative) {
		var preciseGains = mDriveConfig.ballPickupVelocityGains;
		mDistancePidController.setPID(preciseGains.p, preciseGains.i, preciseGains.d);
		mDistancePidController.setSetpoint(0);
		return accelerationDerivative == null ? mDistancePidController.calculate(currentDistance) : mDistancePidController.calculate(currentDistance, accelerationDerivative);
	}

	private void setOutput(double distanceOut, double angleOut) {
		mOutputs.leftOutput.setPercentOutput(clamp(distanceOut - angleOut, -1, 1));
		mOutputs.rightOutput.setPercentOutput(clamp(distanceOut + angleOut, -1, 1));
	}
}
