package com.palyrobotics.frc2020.subsystems.controllers.indexer;

import com.palyrobotics.frc2020.config.constants.IndexerConstants;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;

public class FeedColumnController extends Indexer.IndexerColumnController {

	public FeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Feed Controller");

		double indexerOutput = IndexerConstants.kTargetDistanceToIndexerVelocity.getInterpolated(state.targetDistance);
		mMasterSparkOutput.setPercentOutput(indexerOutput);
		mSlaveSparkOutput.setPercentOutput(indexerOutput);
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return true;
	}
}
