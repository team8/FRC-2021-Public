package com.palyrobotics.frc2020.subsystems.controllers.drive;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.util.Circle;
import com.palyrobotics.frc2020.util.control.SynchronousPIDF;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;

import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;

public class BallAutoAlignController extends Drive.DriveController {

	public static final int kFilterSize = 3;

	private final Timer mTimer = new Timer();

	@Override
	public void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		if (state.driveIsGyroReady) {
			if (state.balls.isEmpty()) {
				// we can't see any balls so there is nothing for us to do. might want to just go straight
				return;
			}

			Circle largest = state.balls.get(0);
			for (int i = 1; i < state.balls.size(); i++) {
				if (state.balls.get(i).radius >= largest.radius) {
					largest = state.balls.get(i);
				}
			}

			var targetGyroYawFilter = new MedianFilter(kFilterSize);
			var targetGyroYaw = targetGyroYawFilter.calculate(state.driveYawDegrees - largest.center.x); // TODO: change from x to something similar to what limelight gives

			SynchronousPIDF pidf = new SynchronousPIDF();
			pidf.setPID(mConfig.velocityGains.p, mConfig.velocityGains.i, mConfig.velocityGains.d);
			pidf.setSetpoint(targetGyroYaw);

			double percentOutput = pidf.calculate(state.driveYawDegrees - state.driveYawAngularVelocityDegrees);
			double staticAdjustedPO = percentOutput + Math.signum(percentOutput) * mConfig.turnGainsS;
			mOutputs.leftOutput.setPercentOutput(-staticAdjustedPO);
			mOutputs.rightOutput.setPercentOutput(staticAdjustedPO);

			// Copied from RamseteDriveController, logging never seems like a bad idea.
			LiveGraph.add("time", mTimer.get());
			LiveGraph.add("currentPoseX", state.drivePoseMeters.getTranslation().getX());
			LiveGraph.add("currentPoseY", state.drivePoseMeters.getTranslation().getY());
			LiveGraph.add("currentPoseR", state.drivePoseMeters.getRotation().getDegrees());
			LiveGraph.add("leftVelocity", state.driveLeftVelocity);
			LiveGraph.add("rightVelocity", state.driveRightVelocity);
		}
	}
}
