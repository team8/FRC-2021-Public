package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;

public class ReverseFeedColumnController extends Indexer.IndexerColumnController {

	public ReverseFeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Reverse Feed Controller");
		mMasterSparkOutput.setPercentOutput(-0.3);
		mSlaveSparkOutput.setPercentOutput(-0.3);
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return super.isFinished(state);
	}
}
