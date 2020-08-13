package com.palyrobotics.frc2020.robot;

import static com.palyrobotics.frc2020.util.Util.handleDeadBand;
import static com.palyrobotics.frc2020.vision.Limelight.kOneTimesZoomPipelineId;
import static com.palyrobotics.frc2020.vision.Limelight.kTwoTimesZoomPipelineId;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.behavior.routines.indexer.IndexerFeedRoutine;
import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.robot.HardwareAdapter.Joysticks;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.input.Joystick;
import com.palyrobotics.frc2020.util.input.XboxController;

/**
 * Used to produce {@link Commands}'s from human input. Should only be used in robot package.
 */
public class OperatorInterface {

	public static final double kDeadBand = 0.05;
	public static final int kOnesTimesZoomAlignButton = 3, kTwoTimesZoomAlignButton = 4;
	private final ShooterConfig mShooterConfig = Configs.get(ShooterConfig.class);
	private final Joystick mDriveStick = Joysticks.getInstance().driveStick,
			mTurnStick = Joysticks.getInstance().turnStick;
	private final XboxController mOperatorXboxController = Joysticks.getInstance().operatorXboxController;
	private final IntakeConfig mIntakeConfig = Configs.get(IntakeConfig.class);

	/**
	 * Modifies commands based on operator input devices.
	 */
	void updateCommands(Commands commands, @ReadOnly RobotState state) {
		commands.shouldClearCurrentRoutines = mDriveStick.getTriggerPressed();
		updateDriveCommands(commands);
		updateSuperstructureCommands(commands, state);
		mOperatorXboxController.updateLastInputs();

		Robot.sLoopDebugger.addPoint("updateCommands");
	}

	private void updateDriveCommands(Commands commands) {
		commands.setDriveSlowTurnLeft(mTurnStick.getPOV(0) == 270);
		commands.setDriveTeleop(
				handleDeadBand(-mDriveStick.getY(), kDeadBand), handleDeadBand(mTurnStick.getX(), kDeadBand),
				mTurnStick.getTrigger(), mTurnStick.getPOV(0) == 90 || mTurnStick.getPOV(0) == 270,
				mDriveStick.getTrigger());
		boolean wantsOneTimesAlign = mTurnStick.getRawButton(kOnesTimesZoomAlignButton),
				wantsTwoTimesAlign = mTurnStick.getRawButton(kTwoTimesZoomAlignButton);
		// Vision align overwrites wanted drive state, using teleop commands when no target is in sight
		if (wantsOneTimesAlign) {
			commands.setDriveVisionAlign(kOneTimesZoomPipelineId);
		} else if (wantsTwoTimesAlign) {
			commands.setDriveVisionAlign(kTwoTimesZoomPipelineId);
		}
	}

	private void updateSuperstructureCommands(Commands commands, RobotState state) {
		if (mOperatorXboxController.getDPadDownReleased()) {
			commands.setIntakeRunning(0);
		} else if (mOperatorXboxController.getDPadDown()) {
			if (!state.intakeStalled) {
				commands.setIntakeRunning(mIntakeConfig.rollerPo);
			} else {
				commands.setIntakeRunning(-mIntakeConfig.rollerPo);
			}
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.FORWARD;
		} else if (mOperatorXboxController.getDPadUp()) {
			commands.setIntakeStowed();
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
		}
		if (mOperatorXboxController.getRightTriggerPressed()) {
			commands.addWantedRoutine(new IndexerFeedRoutine());
		} else if (mOperatorXboxController.getLeftTrigger()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.REVERSE_FEED;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
		} else if ((state.indexerPos1Blocked && !state.indexerPos4Blocked) || mOperatorXboxController.getXButton()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.INDEX;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.FORWARD;
		} else if (mOperatorXboxController.getBButton()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.UN_INDEX;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
		} else {
			commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
		}

		/* Shooting */
		if (mOperatorXboxController.getRightTriggerPressed()) {
			commands.setShooterCustomState(0, Shooter.HoodState.HIGH);
			commands.wantedCompression = false;
		} else if (mOperatorXboxController.getLeftTriggerPressed()) {
			commands.setShooterIdleState();
			commands.visionWanted = false;
			commands.wantedCompression = true;
		}
		/* Turret */
		if (state.inShootingQuadrant) {
			commands.setTurretVisionAlign();
		} else {
			commands.setTurretIdle();
		}

		if (mOperatorXboxController.getRightBumper()) {
			commands.setShooterCustomState(1500, Shooter.HoodState.MEDIUM);
		} else if (mOperatorXboxController.getLeftBumper()) {
			commands.setIntakeStowed();
			commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
			commands.setShooterIdleState();
		}
	}

	public void resetPeriodic(Commands commands) {
	}

	public void reset(Commands commands) {
		commands.routinesWanted.clear();
		commands.setDriveNeutral();
		commands.wantedCompression = true;
		commands.visionWanted = false;
		commands.setIntakeStowed();
		commands.setShooterIdleState();
		commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
		commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
		mOperatorXboxController.clearLastInputs();
	}
}
