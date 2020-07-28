package com.palyrobotics.frc2020.robot;

import java.util.List;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.palyrobotics.frc2020.config.PortConstants;
import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.util.control.Falcon;
import com.palyrobotics.frc2020.util.control.Spark;
import com.palyrobotics.frc2020.util.control.TimedSolenoid;
import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Represents all hardware components of the robot. Singleton class. Should only be used in robot
 * package. Subdivides hardware into subsystems.
 */
public class HardwareAdapter {

	/**
	 * 4 Falcon 500s (controlled by Talon FX), 1 Pigeon IMU Gyro connected via Talon SRX data cable.
	 */
	static class DriveHardware {

		private static DriveHardware sInstance;

		final Falcon leftMasterFalcon = new Falcon(sPortConstants.nariDriveLeftMasterId, "Drive Left Master"),
				leftSlaveFalcon = new Falcon(sPortConstants.nariDriveLeftSlaveId, "Drive Left Slave");
		final Falcon rightMasterFalcon = new Falcon(sPortConstants.nariDriveRightMasterId, "Drive Right Master"),
				rightSlaveFalcon = new Falcon(sPortConstants.nariDriveRightSlaveId, "Drive Right Slave");

		final List<Falcon> falcons = List.of(leftMasterFalcon, leftSlaveFalcon,
				rightMasterFalcon, rightSlaveFalcon);

		final PigeonIMU gyro = new PigeonIMU(sPortConstants.nariDriveGyroId);

		private DriveHardware() {
		}

		static DriveHardware getInstance() {
			if (sInstance == null) sInstance = new DriveHardware();
			return sInstance;
		}
	}

	static class IntakeHardware {

		private static IntakeHardware sInstance;

		final Talon talon = new Talon(sPortConstants.nariIntakeId, "intake");
		final TimedSolenoid solenoid = new TimedSolenoid(sPortConstants.nariIntakeSolenoidId, Configs.get(IntakeConfig.class).solenoidActuationDurationSec, false);

		private IntakeHardware() {
		}

		static IntakeHardware getInstance() {
			if (sInstance == null) sInstance = new IntakeHardware();
			return sInstance;
		}
	}

	/**
	 * 1 TalonSRX and Encoder
	 */
	static class TurretHardware {

		private static TurretHardware sInstance;
		final Talon talon = new Talon(sPortConstants.nariTurretTalonId, "Turret Talon");

		private TurretHardware() {
		}

		static TurretHardware getInstance() {
			if (sInstance == null) sInstance = new TurretHardware();
			return sInstance;
		}
	}

	/**
	 * 2 NEO's, Hood Piston, Blocking Solenoid
	 */
	static class ShooterHardware {

		private static ShooterHardware sInstance;
		final Spark masterSpark = new Spark(sPortConstants.nariShooterMasterId, "Shooter Master"), slaveSpark = new Spark(sPortConstants.nariShooterSlaveId, "Shooter Slave");
		final CANEncoder masterEncoder = masterSpark.getEncoder();
		final TimedSolenoid blockingSolenoid = new TimedSolenoid(sPortConstants.nariShooterBlockingSolenoidId, 0.2, false),
				hoodPiston = new TimedSolenoid(sPortConstants.nariShooterBlockingSolenoidId, 0.2, false);

		static ShooterHardware getInstance() {
			if (sInstance == null) sInstance = new ShooterHardware();
			return sInstance;
		}
	}

	/*
	 * 2 NEOs (controlled by Spark MAX) for indexer tower, 2 775s (controlled by Talon SRX) for v belts,
	 * 2 Single Solenoids, 2 Infrared Sensors
	 */
	static class IndexerHardware {

		private static IndexerHardware sInstance;

		final Spark slaveColumnSpark = new Spark(sPortConstants.nariIndexerSlaveId, "slaveColumnSpark"),
				masterColumnSpark = new Spark(sPortConstants.nariIndexerMasterId, "masterColumnSpark");
		final CANEncoder masterColumnSparkEncoder = masterColumnSpark.getEncoder();
		final CANEncoder slaveColumnSparkEncoder = slaveColumnSpark.getEncoder();
		final List<Spark> columnSparks = List.of(masterColumnSpark, slaveColumnSpark);
		final Talon leftVTalon = new Talon(sPortConstants.nariIndexerLeftVTalonId, "leftVTalon"),
				rightVTalon = new Talon(sPortConstants.nariIndexerRightVTalonId, "rightVTalon");
		final List<Talon> vTalons = List.of(rightVTalon, leftVTalon);
		final Solenoid blockingSolenoid = new Solenoid(sPortConstants.nariIndexerBlockingSolenoidId),
				hopperSolenoids = new Solenoid(sPortConstants.nariIndexerHopperSolenoidId);
		final DigitalInput pos1Sensor = new DigitalInput(sPortConstants.nariIndexerPos1Infrared);
		final DigitalInput pos4Sensor = new DigitalInput(sPortConstants.nariIndexerPos4Infrared);

		private IndexerHardware() {

		}

		static IndexerHardware getInstance() {
			if (sInstance == null) sInstance = new IndexerHardware();
			return sInstance;
		}
	}

	/**
	 * 2 NEO (controlled by Spark MAX), 3 Solenoids
	 */
	static class ShooterHardware {

		private static ShooterHardware sInstance;
		final Spark masterSpark = new Spark(sPortConstants.nariShooterMasterId, "Shooter Master"),
				slaveSpark = new Spark(sPortConstants.nariShooterSlaveId, "Shooter Slave");
		final CANEncoder masterEncoder = masterSpark.getEncoder();
		final TimedSolenoid hoodSolenoid = new TimedSolenoid(sPortConstants.nariShooterHoodSolenoid, 0.4, true),
				blockingSolenoid = new TimedSolenoid(sPortConstants.nariShooterBlockingSolenoidId, 0.2, false);

		private ShooterHardware() {
		}

		static ShooterHardware getInstance() {
			if (sInstance == null) sInstance = new ShooterHardware();
			return sInstance;
		}
	}


	/**
	 * 1 Compressor, 1 PDP, 1 Fisheye USB Camera
	 */
	static class MiscellaneousHardware {

		private static MiscellaneousHardware sInstance;
		final Compressor compressor = new Compressor();
		final PowerDistributionPanel pdp = new PowerDistributionPanel();
//    final UsbCamera fisheyeCam = CameraServer.getInstance().startAutomaticCapture();

		private MiscellaneousHardware() {
			compressor.stop();
		}

		static MiscellaneousHardware getInstance() {
			if (sInstance == null) sInstance = new MiscellaneousHardware();
			return sInstance;
		}
	}

	/**
	 * 2 Joysticks, 1 Xbox Controller
	 */
	static class Joysticks {

		private static final Joysticks sInstance = new Joysticks();
		final Joystick driveStick = new Joystick(0), turnStick = new Joystick(1);
		final XboxController operatorXboxController = new XboxController(2);

		private Joysticks() {
		}

		static Joysticks getInstance() {
			return sInstance;
		}
	}

	private static final PortConstants sPortConstants = Configs.get(PortConstants.class);

	private HardwareAdapter() {
	}
}
