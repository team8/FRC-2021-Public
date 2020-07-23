package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class FeedColumnController extends Indexer.IndexerColumnController {

	public FeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected ControllerOutput update(RobotState robotState) {
		System.out.println("Running Feed Controller");
		mOutputs.setPercentOutput(0.3);
		return super.update(robotState);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
}
