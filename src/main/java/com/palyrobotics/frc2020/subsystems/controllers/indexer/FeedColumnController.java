package com.palyrobotics.frc2020.subsystems.controllers.indexer;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;

public class FeedColumnController extends Indexer.IndexerColumnController {

	public FeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running Feed Controller");
		//todo: fix
		mMasterSparkOutput.setPercentOutput(0.8);
		mSlaveSparkOutput.setPercentOutput(0.8);
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return true;
	}
}
