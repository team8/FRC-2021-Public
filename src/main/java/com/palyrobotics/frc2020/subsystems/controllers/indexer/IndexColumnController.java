package com.palyrobotics.frc2020.subsystems.controllers.indexer;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class IndexColumnController extends Indexer.IndexerColumnController {

	private final IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	private final PIDController mMasterSparkPIDController = new PIDController(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
	private final PIDController mSlaveSparkPIDController = new PIDController(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);
	private final double mMasterSparkEncWantedPosition, mSlaveSparkEncWantedPosition;
	private final Timer mTimer = new Timer();

	public IndexColumnController(RobotState state) {
		super(state);
		mTimer.start();
		mMasterSparkEncWantedPosition = state.indexerMasterEncPosition + mConfig.powercellIndexDistance - 4;
		mSlaveSparkEncWantedPosition = state.indexerSlaveEncPosition + mConfig.powercellIndexDistance;
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Index Controller");
		mMasterSparkPIDController.setPID(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
		mSlaveSparkPIDController.setPID(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);

		mMasterSparkOutput.setPercentOutput(MathUtil.clamp(mMasterSparkPIDController.calculate(state.indexerMasterEncPosition, mMasterSparkEncWantedPosition), -mConfig.maximumIndexerColumnPo, mConfig.maximumIndexerColumnPo));
		mSlaveSparkOutput.setPercentOutput(MathUtil.clamp(mSlaveSparkPIDController.calculate(state.indexerSlaveEncPosition, mSlaveSparkEncWantedPosition), -mConfig.maximumIndexerColumnPo, mConfig.maximumIndexerColumnPo));
		if (mTimer.get() > mConfig.indexDuration) {
			mRightVTalonOutput.setPercentOutput(mConfig.rightVTalonPo);
			mLeftVTalonOutput.setPercentOutput(mConfig.leftVTalonPo);
		} else {
			mRightVTalonOutput.setIdle();
			mLeftVTalonOutput.setIdle();
		}
		LiveGraph.add("MasterSparkPo", MathUtil.clamp(mMasterSparkPIDController.calculate(state.indexerMasterEncPosition, mMasterSparkEncWantedPosition), -mConfig.maximumIndexerColumnPo, mConfig.maximumIndexerColumnPo));
		LiveGraph.add("MasterTarget", mMasterSparkEncWantedPosition);
		LiveGraph.add("SlaveTarget", mSlaveSparkEncWantedPosition);
		LiveGraph.add("isFinished", Math.abs(mMasterSparkEncWantedPosition - state.indexerMasterEncPosition));
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return Math.abs(mMasterSparkEncWantedPosition - state.indexerMasterEncPosition) < mConfig.indexFinishedMinThreshold && Math.abs(mSlaveSparkEncWantedPosition - state.indexerSlaveEncPosition) < mConfig.indexFinishedMinThreshold && mTimer.get() > mConfig.indexControllerDuration;
	}
}
