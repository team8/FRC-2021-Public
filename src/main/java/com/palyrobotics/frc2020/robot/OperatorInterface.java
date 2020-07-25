package com.palyrobotics.frc2020.robot;

import static com.palyrobotics.frc2020.util.Util.handleDeadBand;
import static com.palyrobotics.frc2020.vision.Limelight.kOneTimesZoomPipelineId;
import static com.palyrobotics.frc2020.vision.Limelight.kTwoTimesZoomPipelineId;


import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.HardwareAdapter.Joysticks;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.input.Joystick;
import com.palyrobotics.frc2020.util.input.XboxController;
import com.palyrobotics.frc2020.vision.Limelight;

/**
 * Used to produce {@link Commands}'s from human input. Should only be used in robot package.
 */
public class OperatorInterface {

	public static final double kDeadBand = 0.05;
	public static final int kOnesTimesZoomAlignButton = 3, kTwoTimesZoomAlignButton = 4;
	private final ShooterConfig mShooterConfig = Configs.get(ShooterConfig.class);
	private final Joystick mDriveStick = Joysticks.getInstance().driveStick,
			mTurnStick = Joysticks.getInstance().turnStick;
	private Limelight mLimelight = Limelight.getInstance();
	private final XboxController mOperatorXboxController = Joysticks.getInstance().operatorXboxController;

	/**
	 * Modifies commands based on operator input devices.
	 */
	void updateCommands(Commands commands, @ReadOnly RobotState state) {
		commands.shouldClearCurrentRoutines = mDriveStick.getTriggerPressed();
		updateDriveCommands(commands);
		updateSuperstructure(commands, state);
		updateIntakeCommands(commands);
		updateIndexerCommands(commands, state);

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

	private void updateSuperstructure(Commands commands, RobotState state) {
		/* Shooting */
		if (mOperatorXboxController.getRightTriggerPressed()) {
			commands.setShooterWantedCustomFlywheelVelocity(mShooterConfig.noVisionVelocity);
			commands.setShooterVisionAssisted(commands.visionWantedPipeline);
			commands.wantedCompression = false;
		} else if (mOperatorXboxController.getLeftTriggerPressed()) {
			commands.setShooterIdle();
			commands.visionWanted = false;
			commands.wantedCompression = true;
		}

		/* Turret */
		if (state.inShootingQuadrant) {
			commands.setTurretVisionAlign();
		} else {
			commands.setTurretIdle();
		}
	}

	private void updateIntakeCommands(Commands commands) {
		if (mOperatorXboxController.getDPadLeft()) {
			commands.intakeWantedState = Intake.State.INTAKE;
		} else {
			commands.intakeWantedState = Intake.State.IDLE;
		}
	}

	private void updateIndexerCommands(Commands commands, RobotState state) {
		if (mOperatorXboxController.getDPadRight()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.FEED;
		} else if (mOperatorXboxController.getDPadDown()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.REVERSE_FEED;
		} else if (state.indexerPos1Blocked && !state.indexerPos4Blocked) {
			commands.indexerColumnWantedState = Indexer.ColumnState.INDEX;
		} else {
			commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
		}
		if (mOperatorXboxController.getYButton()) {
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.FORWARD;
		}
		else if (mOperatorXboxController.getAButton()) {
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
		}
		else {
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
		}
	}

	public void resetPeriodic(Commands commands) {
	}

	public void reset(Commands commands) {
		commands.routinesWanted.clear();
		commands.setDriveNeutral();
		commands.wantedCompression = true;
		commands.visionWanted = false;
		commands.intakeWantedState = Intake.State.IDLE;
		commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
		commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
		mOperatorXboxController.clearLastInputs();
	}
}
