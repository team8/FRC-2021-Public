package com.palyrobotics.frc2020.robot;

import java.util.Set;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.PigeonIMU_StatusFrame;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.config.RobotConfig;
import com.palyrobotics.frc2020.config.constants.DriveConstants;
import com.palyrobotics.frc2020.robot.RobotState.GamePeriod;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.Falcon;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class HardwareWriter {

	public static final int
	// Blocks config calls for specified timeout
	kTimeoutMs = 150,
			// Different from slot index.
			// 0 for Primary closed-loop. 1 for auxiliary closed-loop.
			kPidIndex = 0;
	private static final String kLoggerTag = Util.classToJsonName(HardwareWriter.class);
	public static final double kVoltageCompensation = 12.0;
	public static final SupplyCurrentLimitConfiguration k30AmpCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(
			true, 30.0, 35.0, 1.0);
	private final RobotConfig mRobotConfig = Configs.get(RobotConfig.class);
	private final Drive mDrive = Drive.getInstance();
	private boolean mRumbleOutput;

	void configureHardware(Set<SubsystemBase> enabledSubsystems) {
		if (enabledSubsystems.contains(mDrive)) configureDriveHardware();
		configureMiscellaneousHardware();
	}

	private void configureMiscellaneousHardware() {
		var hardware = HardwareAdapter.MiscellaneousHardware.getInstance();
		hardware.pdp.clearStickyFaults();
		hardware.compressor.clearAllPCMStickyFaults();
	}

	private void configureDriveHardware() {
		var hardware = HardwareAdapter.DriveHardware.getInstance();
		/* Falcons */
		for (Falcon falcon : hardware.falcons) {
			falcon.configFactoryDefault(kTimeoutMs);
			falcon.enableVoltageCompensation(true);
			falcon.configVoltageCompSaturation(kVoltageCompensation, kTimeoutMs);
			falcon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, kPidIndex, kTimeoutMs);
			falcon.configIntegratedSensorInitializationStrategy(SensorInitializationStrategy.BootToZero, kTimeoutMs);
			falcon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(
					true, 40.0, 50.0, 1.0));
			falcon.configOpenloopRamp(0.1, kTimeoutMs);
			falcon.configClosedloopRamp(0.1, kTimeoutMs);
			falcon.configSensorConversions(DriveConstants.kDriveMetersPerTick, DriveConstants.kDriveMetersPerSecondPerTickPer100Ms);
		}
		// Left
		hardware.leftMasterFalcon.setInverted(false);
		hardware.leftMasterFalcon.setFrameTimings(5, 5);
		hardware.leftSlaveFalcon.follow(hardware.leftMasterFalcon);
		hardware.leftSlaveFalcon.setInverted(InvertType.FollowMaster);
		hardware.leftSlaveFalcon.setFrameTimings(40, 40);
		// Right
		hardware.rightMasterFalcon.setInverted(true);
		hardware.rightMasterFalcon.setFrameTimings(5, 5);
		hardware.rightSlaveFalcon.follow(hardware.rightMasterFalcon);
		hardware.rightSlaveFalcon.setInverted(InvertType.FollowMaster);
		hardware.rightSlaveFalcon.setFrameTimings(40, 40);
		/* Gyro */
		// 10 ms update period for yaw degrees and yaw angular velocity in degrees per second
		setPigeonStatusFramePeriods(hardware.gyro);
		/* Falcons and Gyro */
		resetDriveSensors(new Pose2d());
	}

	public void resetDriveSensors(Pose2d pose) {
		double heading = pose.getRotation().getDegrees();
		var hardware = HardwareAdapter.DriveHardware.getInstance();
		hardware.gyro.setYaw(heading, kTimeoutMs);
		hardware.leftMasterFalcon.setSelectedSensorPosition(0);
		hardware.rightMasterFalcon.setSelectedSensorPosition(0);
		Log.info(kLoggerTag, String.format("Drive sensors reset, gyro heading: %s", heading));
	}

	void setDriveNeutralMode(NeutralMode neutralMode) {
		var hardware = HardwareAdapter.DriveHardware.getInstance();
		hardware.leftMasterFalcon.setNeutralMode(neutralMode);
		hardware.rightMasterFalcon.setNeutralMode(neutralMode);
	}

	/**
	 * Updates the hardware to run with output values of {@link SubsystemBase}'s.
	 */
	void writeHardware(Set<SubsystemBase> enabledSubsystems, @ReadOnly RobotState robotState) {
		mRumbleOutput = false;
		if (!mRobotConfig.disableHardwareUpdates) {
			if (enabledSubsystems.contains(mDrive) && robotState.gamePeriod != GamePeriod.TESTING) updateDrivetrain();
			Robot.sLoopDebugger.addPoint("writeDrive");
		}
		var joystickHardware = HardwareAdapter.Joysticks.getInstance();
		joystickHardware.operatorXboxController.setRumble(mRumbleOutput);
		Robot.sLoopDebugger.addPoint("writeHardware");
	}

	private void updateDrivetrain() {
		var hardware = HardwareAdapter.DriveHardware.getInstance();
		hardware.falcons.forEach(Falcon::handleReset);
		hardware.leftMasterFalcon.setOutput(mDrive.getDriveSignal().leftOutput);
		hardware.rightMasterFalcon.setOutput(mDrive.getDriveSignal().rightOutput);
		handleReset(hardware.gyro);
	}

	private void setPigeonStatusFramePeriods(PigeonIMU gyro) {
		gyro.setStatusFramePeriod(PigeonIMU_StatusFrame.CondStatus_9_SixDeg_YPR, 5, kTimeoutMs);
		gyro.setStatusFramePeriod(PigeonIMU_StatusFrame.BiasedStatus_2_Gyro, 5, kTimeoutMs);
	}

	private void handleReset(PigeonIMU pigeon) {
		if (pigeon.hasResetOccurred()) {
			Log.error(kLoggerTag, "Pigeon reset");
			setPigeonStatusFramePeriods(pigeon);
		}
	}
}
