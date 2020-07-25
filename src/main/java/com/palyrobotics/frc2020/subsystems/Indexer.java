package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.controllers.indexer.FeedColumnController;
import com.palyrobotics.frc2020.subsystems.controllers.indexer.IndexColumnController;
import com.palyrobotics.frc2020.subsystems.controllers.indexer.ReverseFeedColumnController;
import com.palyrobotics.frc2020.subsystems.controllers.indexer.UnIndexColumnController;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Indexer extends SubsystemBase {

	public enum State {
		INDEX, UN_INDEX, FEED, REVERSE_FEED, IDLE
	}

	public abstract static class IndexerColumnController {

		protected ControllerOutput mMasterSparkOutput = new ControllerOutput(), mSlaveSparkOutput = new ControllerOutput(),
				mRightVTalonOutput = new ControllerOutput(), mLeftVTalonOutput = new ControllerOutput();

		protected IndexerColumnController(@ReadOnly RobotState state) {
		}

		protected void update(@ReadOnly RobotState state) {
		}

		protected boolean isFinished(@ReadOnly RobotState state) {
			return true;
		}
	}

	private static final Indexer sInstance = new Indexer();
	private static final IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	private static State mActiveState = State.IDLE;
	private static IndexerColumnController mRunningController = null;
	private static ControllerOutput mMasterIndexerColumnOutput = new ControllerOutput(),
			mSlaveIndexerColumnOutput = new ControllerOutput(), mRightVTalonOutput = new ControllerOutput(),
			mLeftVTalonOutput = new ControllerOutput();
	private static boolean mBlockingSolenoidOutput, mHopperSolenoidOutput;

	public static Indexer getInstance() {
		return sInstance;
	}

	@Override
	public void update(Commands commands, RobotState state) {
		boolean isNewState = mRunningController == null || mRunningController.isFinished(state);

		if (isNewState) {
			switch (commands.indexerWantedState) {
				case FEED:
					mActiveState = State.FEED;
					mRunningController = new FeedColumnController(state);
					break;
				case REVERSE_FEED:
					mActiveState = State.REVERSE_FEED;
					mRunningController = new ReverseFeedColumnController(state);
					break;
				case INDEX:
					mActiveState = State.INDEX;
					mRunningController = new IndexColumnController(state);
					break;
				case UN_INDEX:
					mActiveState = State.UN_INDEX;
					mRunningController = new UnIndexColumnController(state);
					break;
				default:
					mActiveState = State.IDLE;
					mRunningController = null;
			}
		}
		if (mRunningController != null) {
			mRunningController.update(state);
			mMasterIndexerColumnOutput = mRunningController.mMasterSparkOutput;
			mSlaveIndexerColumnOutput = mRunningController.mSlaveSparkOutput;
			mRightVTalonOutput = mRunningController.mRightVTalonOutput;
			mLeftVTalonOutput = mRunningController.mLeftVTalonOutput;
		} else {
			System.out.println("Indexer @ Idle");
			mMasterIndexerColumnOutput.setIdle();
			mSlaveIndexerColumnOutput.setIdle();
			mRightVTalonOutput.setIdle();
			mLeftVTalonOutput.setIdle();
		}
		mBlockingSolenoidOutput = !mConfig.blockingSolenoidExtended;
		mHopperSolenoidOutput = mConfig.hopperSolenoidExtended;
	}

	public ControllerOutput getRightVTalonOutput() {
		return mRightVTalonOutput;
	}

	public ControllerOutput getLeftVTalonOutput() {
		return mLeftVTalonOutput;
	}

	public ControllerOutput getMasterIndexerColumnOutput() {
		return mMasterIndexerColumnOutput;
	}

	public ControllerOutput getSlaveIndexerColumnOutput() {
		return mSlaveIndexerColumnOutput;
	}

	public boolean getBlockingSolenoidOutput() {
		return mBlockingSolenoidOutput;
	}

	public boolean getHopperSolenoidOutput() {
		return mHopperSolenoidOutput;
	}
}
