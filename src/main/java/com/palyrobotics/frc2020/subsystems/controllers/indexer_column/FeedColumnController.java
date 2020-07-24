package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;

public class FeedColumnController extends Indexer.IndexerColumnController {

	private IndexerConfig mConfig = Configs.get(IndexerConfig.class);

	public FeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Feed Controller");
		mMasterSparkOutput.setPercentOutput(0.3);
		mSlaveSparkOutput.setPercentOutput(0.3);
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return super.isFinished(state);
	}
}
