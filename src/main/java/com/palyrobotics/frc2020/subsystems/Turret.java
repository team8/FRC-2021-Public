package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.TurretConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.Timer;

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
				mOutput.setTargetPosition(getLatencyCompensatedYaw2Target(state) + state.turretYawDegrees, mConfig.turretGains);
				break;
			case CUSTOM_ANGLE_SETPOINT:
				mOutput.setTargetPosition(commands.getTurretWantedAngle(), mConfig.turretGains);
				break;
			case IDLE:
				mOutput.setIdle();
		}
	}

	private double getLatencyCompensatedYaw2Target(RobotState state) {
		double timestampPose = Timer.getFPGATimestamp() - 11 - mLimelight.getPipelineLatency(); //11 ms reduction because of image capture latency
		Double floorKey = state.pastPoses.floorKey(timestampPose),
				ceilingKey = state.pastPoses.ceilingKey(timestampPose);
		return (floorKey == null) ? state.pastPoses.get(ceilingKey).getValue1() :
				(ceilingKey == null) ? state.pastPoses.get(floorKey).getValue1() :
						(ceilingKey - timestampPose > timestampPose - floorKey) ? state.pastPoses.get(floorKey).getValue1() : state.pastPoses.get(ceilingKey).getValue1();
	}

	public ControllerOutput getOutput() {
		return mOutput;
	}
}
