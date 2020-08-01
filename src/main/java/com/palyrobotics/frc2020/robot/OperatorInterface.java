package com.palyrobotics.frc2020.robot;

import static com.palyrobotics.frc2020.util.Util.handleDeadBand;
import static com.palyrobotics.frc2020.vision.Limelight.kOneTimesZoomPipelineId;
import static com.palyrobotics.frc2020.vision.Limelight.kTwoTimesZoomPipelineId;

import com.palyrobotics.frc2020.behavior.routines.indexer.IndexerFeedRoutine;
import com.palyrobotics.frc2020.robot.HardwareAdapter.Joysticks;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.subsystems.Intake;
import com.palyrobotics.frc2020.util.input.Joystick;
import com.palyrobotics.frc2020.util.input.XboxController;
import com.palyrobotics.frc2020.vision.Limelight;

/**
 * Used to produce {@link Commands}'s from human input. Should only be used in robot package.
 */
public class OperatorInterface {

	public static final double kDeadBand = 0.05;
	public static final int kOnesTimesZoomAlignButton = 3, kTwoTimesZoomAlignButton = 4;
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
		/* Path Following */
//		if (mOperatorXboxController.getBButtonPressed()) {
//			commands.addWantedRoutine(new SequentialRoutine(
//					new DriveSetOdometryRoutine(0.0, 0.0, 0.0),
//					new DrivePathRoutine(newWaypoint(30.0, 0.0, 0.0))));
//			commands.addWantedRoutine(new SequentialRoutine(
//					new DriveSetOdometryRoutine(0.0, 0.0, 180.0),
//					new DriveYawRoutine(0.0)));
//			commands.addWantedRoutine(new DrivePathRoutine(newWaypoint(0.0, 0.0, 180.0)));
//			commands.addWantedRoutine(new SequentialRoutine(
//					new DriveSetOdometryRoutine(0.0, 0.0, 0.0),
//					new DrivePathRoutine(newWaypoint(40.0, 0.0, 0.0))
//							.setMovement(1.0, 1.0)
//							.endingVelocity(0.5),
//					new DrivePathRoutine(newWaypoint(80.0, 0.0, 0.0))
//							.setMovement(0.5, 1.0)
//							.startingVelocity(0.5)));
//		}
	}

	private void updateSuperstructureCommands(Commands commands, RobotState state) {
		if (mOperatorXboxController.getYButton()) {
			commands.intakeWantedState = Intake.State.INTAKE;
			commands.indexerVSingulatorWantedState = state.indexerPos1Blocked ? Indexer.VSingulatorState.IDLE : Indexer.VSingulatorState.FORWARD;
		} else if (mOperatorXboxController.getAButton()) {
			commands.intakeWantedState = Intake.State.REVERSE;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
		} else {
			commands.intakeWantedState = Intake.State.IDLE;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.IDLE;
		}
		if (mOperatorXboxController.getRightTriggerPressed()) {
			commands.addWantedRoutine(new IndexerFeedRoutine());
		} else if (mOperatorXboxController.getLeftTrigger()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.REVERSE_FEED;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
			commands.intakeWantedState = Intake.State.REVERSE;
		} else if (state.indexerPos1Blocked && !state.indexerPos4Blocked || mOperatorXboxController.getDPadUp()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.INDEX;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.FORWARD;
		} else if (mOperatorXboxController.getDPadDown()) {
			commands.indexerColumnWantedState = Indexer.ColumnState.UN_INDEX;
			commands.indexerVSingulatorWantedState = Indexer.VSingulatorState.REVERSE;
		} else {
			commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
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
