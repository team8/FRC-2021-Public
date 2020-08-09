package com.palyrobotics.frc2020.robot;

import java.util.Set;

import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.sensors.PigeonIMU.PigeonState;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.RobotConfig;
import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.robot.HardwareAdapter.DriveHardware;
import com.palyrobotics.frc2020.robot.HardwareAdapter.IntakeHardware;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.Falcon;
import com.palyrobotics.frc2020.util.control.Spark;
import com.palyrobotics.frc2020.util.control.Talon;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;
import com.revrobotics.CANSparkMax.FaultID;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class HardwareReader {

	private static final String kLoggerTag = Util.classToJsonName(HardwareReader.class);
	private static final int kYawIndex = 0, kYawAngularVelocityIndex = 2;
	private static final Timer mTimer = new Timer();
	private final RobotConfig mRobotConfig = Configs.get(RobotConfig.class);
	private final IntakeConfig mIntakeConfig = Configs.get(IntakeConfig.class);

	private final double[] mGyroAngles = new double[3], mGyroAngularVelocities = new double[3];

	public HardwareReader() {
		mTimer.start();
	}

	/**
	 * Takes all of the sensor data from the hardware, and unwraps it into the current
	 * {@link RobotState}.
	 */
	void readState(Set<SubsystemBase> enabledSubsystems, RobotState state) {
		readGameAndFieldState(state);
		if (enabledSubsystems.contains(Drive.getInstance())) readDriveState(state);
		if (enabledSubsystems.contains(Intake.getInstance())) readIntakeState(state);
		if (enabledSubsystems.contains(Indexer.getInstance())) readIndexerState(state);
		readJoystickState(state);
	}

	private void readGameAndFieldState(RobotState state) {
		state.gameData = DriverStation.getInstance().getGameSpecificMessage();
		state.gameTime = mTimer.get();
	}

	private void readDriveState(RobotState state) {
		var hardware = DriveHardware.getInstance();
		/* Gyro */
		state.driveIsGyroReady = hardware.gyro.getState() == PigeonState.Ready;
		if (state.driveIsGyroReady) {
			hardware.gyro.getYawPitchRoll(mGyroAngles);
			state.driveYawDegrees = mGyroAngles[kYawIndex];
			hardware.gyro.getRawGyro(mGyroAngularVelocities);
			state.driveYawAngularVelocityDegrees = mGyroAngularVelocities[kYawAngularVelocityIndex];
		}
		/* Falcons */
		state.driveLeftVelocity = hardware.leftMasterFalcon.getConvertedVelocity();
		state.driveRightVelocity = hardware.rightMasterFalcon.getConvertedVelocity();
		state.driveLeftPosition = hardware.leftMasterFalcon.getConvertedPosition();
		state.driveRightPosition = hardware.rightMasterFalcon.getConvertedPosition();

		/* Odometry */
		state.updateOdometry(state.driveYawDegrees, state.driveLeftPosition, state.driveRightPosition);
		hardware.falcons.forEach(this::checkFalconFaults);
	}

	private void readIndexerState(RobotState state) {
		var hardware = HardwareAdapter.IndexerHardware.getInstance();
		state.indexerPos1Blocked = !hardware.pos1Sensor.get();
		state.indexerPos4Blocked = !hardware.pos4Sensor.get();
		state.indexerMasterEncPosition = hardware.masterColumnSparkEncoder.getPosition();
		state.indexerMasterEncVelocity = hardware.masterColumnSparkEncoder.getVelocity();
		state.indexerSlaveEncPosition = hardware.slaveColumnSparkEncoder.getPosition();
		state.indexerSlaveEncVelocity = hardware.slaveColumnSparkEncoder.getVelocity();
		state.indexerRightVTalonCurrentDraw = hardware.rightVTalon.getStatorCurrent();
		state.indexerLeftVTalonCurrentDraw = hardware.leftVTalon.getStatorCurrent();
		LiveGraph.add("masterIndexerEncoderPosition", state.indexerMasterEncPosition);
		LiveGraph.add("masterIndexerEncoderVelocity", state.indexerMasterEncVelocity);
		LiveGraph.add("slaveIndexerEncoderPosition", state.indexerSlaveEncPosition);
		LiveGraph.add("rightVTalonCurrentDraw", hardware.rightVTalon.getStatorCurrent());
		LiveGraph.add("leftVTalonCurrentDraw", hardware.leftVTalon.getStatorCurrent());
	}

	private void readIntakeState(RobotState state) {
		var hardware = IntakeHardware.getInstance();
		state.intakeExtended = hardware.solenoid.get();
		state.intakeTransitioning = hardware.solenoid.isInTransition();
		state.intakeStalled = hardware.talon.getStatorCurrent() >= mIntakeConfig.rollerStallCurrent;
		LiveGraph.add("intakeCurrentDraw", hardware.talon.getStatorCurrent());
	}

	private void readJoystickState(RobotState state) {
		var joystickHardware = HardwareAdapter.Joysticks.getInstance();
		state.joystickRightTriggerPressed = joystickHardware.operatorXboxController.getRightTrigger();
	}

	private void checkTalonFaults(Talon talon) {
		if (mRobotConfig.checkFaults) {
			var faults = new StickyFaults();
			talon.getStickyFaults(faults);
			if (faults.hasAnyFault()) {
				Log.error(kLoggerTag, String.format("%s faults: %s", talon.getName(), faults));
				talon.clearStickyFaults();
			}
		}
	}

	private void checkSparkFaults(Spark spark) {
		if (mRobotConfig.checkFaults) {
			boolean wasAnyFault = false;
			for (var value : FaultID.values()) {
				boolean isFaulted = spark.getStickyFault(value);
				if (isFaulted) {
					Log.error(kLoggerTag, String.format("Spark %d fault: %s", spark.getDeviceId(), value));
					wasAnyFault = true;
				}
			}
			if (wasAnyFault) {
				spark.clearFaults();
			}
		}
	}

	private void checkFalconFaults(Falcon falcon) {
		if (mRobotConfig.checkFaults) {
			var faults = new StickyFaults();
			falcon.getStickyFaults(faults);
			if (faults.hasAnyFault()) {
				Log.error(kLoggerTag, String.format("%s faults: %s", falcon.getName(), faults));
				falcon.clearStickyFaults();
			}
		}
	}
}
