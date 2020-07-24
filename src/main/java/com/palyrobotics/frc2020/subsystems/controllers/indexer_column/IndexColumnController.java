package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class IndexColumnController extends Indexer.IndexerColumnController {

	private final IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	private final PIDController mMasterSparkPIDController = new PIDController(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
	private final PIDController mSlaveSparkPIDController = new PIDController(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);
	private final double mMasterSparkEncWantedPosition, mSlaveSparkEncWantedPosition;

	public IndexColumnController(RobotState state) {
		super(state);
		mMasterSparkEncWantedPosition = state.indexerMasterEncPosition + mConfig.powercellIndexDistance - 4
		;
		mSlaveSparkEncWantedPosition = state.indexerSlaveEncPosition + mConfig.powercellIndexDistance;
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Index Controller");
		mMasterSparkPIDController.setPID(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
		mSlaveSparkPIDController.setPID(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);
		LiveGraph.add("MasterTarget", mMasterSparkEncWantedPosition);
		LiveGraph.add("SlaveTarget", mSlaveSparkEncWantedPosition);
		LiveGraph.add("isFinished", Math.abs(mMasterSparkEncWantedPosition - state.indexerMasterEncPosition));
		mMasterSparkOutput.setPercentOutput(MathUtil.clamp(mMasterSparkPIDController.calculate(state.indexerMasterEncPosition, mMasterSparkEncWantedPosition), -0.3, 0.3));
		mSlaveSparkOutput.setPercentOutput(MathUtil.clamp(mSlaveSparkPIDController.calculate(state.indexerSlaveEncPosition, mSlaveSparkEncWantedPosition), -0.3, 0.3));
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return Math.abs(mMasterSparkEncWantedPosition - state.indexerMasterEncPosition) < mConfig.indexFinishedMinThreshold && Math.abs(mSlaveSparkEncWantedPosition - state.indexerSlaveEncPosition) < mConfig.indexFinishedMinThreshold;
	}
}
