package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.constants.TurretConstants;
import com.palyrobotics.frc2020.config.subsystem.TurretConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.util.Units;

public class Turret extends SubsystemBase {

	public enum TurretState {
		IDLE, VISION_ALIGN, CUSTOM_ANGLE_SETPOINT
	}

	private static Turret sInstance = new Turret();
	private ControllerOutput mOutput = new ControllerOutput();
	private TurretConfig mConfig = Configs.get(TurretConfig.class);
	private Limelight mLimelight = Limelight.getInstance();

	private Turret() {
	}

	public static Turret getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		switch (commands.getTurretWantedState()) {
			case VISION_ALIGN:
				/*
				Need to implement the following:
				Latency compensation, accurate feedback control, feedback control with nonzero velocity reference
				Accurate feedforward that accounts for drivetrain motion
				*/

				double latencyCompensationLookBack = Timer.getFPGATimestamp() - mLimelight.imageCaptureLatency / 100.0 - mLimelight.getPipelineLatency() / 100.0;
				Pose2d visionPose = new Pose2d(TurretConstants.targetFieldLocation.minus(new Translation2d(Units.inchesToMeters(mLimelight.getPnPTranslationX()), Units.inchesToMeters(mLimelight.getPnPTranslationY()))), new Rotation2d(Math.toRadians(mLimelight.getPnPYaw())));
				Pose2d adjustedPose = state.drivePoseMeters.plus(visionPose.minus(state.pastPoses.get(latencyCompensationLookBack).getValue2()));
				Transform2d poseChange = adjustedPose.minus(state.pastPoses.get(Timer.getFPGATimestamp() - mConfig.poseChangeLookBackSec).getValue2());
				Pose2d nextDrivePredictedPose = adjustedPose.exp(new Twist2d(poseChange.getTranslation().getX(), poseChange.getTranslation().getY(), poseChange.getRotation().getRadians()));
				Transform2d drivetrain2TurretTransform = new Transform2d(new Translation2d(TurretConstants.drivetrain2TurretX, TurretConstants.drivetrain2TurretY), nextDrivePredictedPose.getRotation()); //fine tune this...
				Pose2d nextTurretPredictedPose = nextDrivePredictedPose.transformBy(drivetrain2TurretTransform);

//				mOutput.setTargetPosition(getLatencyCompensatedYaw2Target(state) + state.turretYawDegrees, mConfig.turretGains);
				break;
			case CUSTOM_ANGLE_SETPOINT:
				mOutput.setTargetPosition(commands.getTurretWantedAngle(), mConfig.turretGains);
				break;
			case IDLE:
				mOutput.setTargetPosition(TurretConstants.turretAngleHardStopRange / 2, mConfig.turretGains);
		}
	}

	public ControllerOutput getOutput() {
		return mOutput;
	}
}
