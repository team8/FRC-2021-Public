package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class ReverseFeedColumnController extends Indexer.IndexerColumnController {

	public ReverseFeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState robotState) {
		System.out.println("Running Reverse Feed Controller");
		mMasterSparkOutput.setPercentOutput(-0.3);
		mSlaveSparkOutput.setPercentOutput(-0.3);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}

	@Override
	protected ControllerOutput getMasterSparkOutput() {
		return mMasterSparkOutput;
	}

	@Override
	protected ControllerOutput getSlaveSparkOutput() {
		return mSlaveSparkOutput;
	}
}
