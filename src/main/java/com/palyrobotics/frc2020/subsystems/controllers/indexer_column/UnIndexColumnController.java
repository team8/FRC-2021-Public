package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;

public class UnIndexColumnController extends Indexer.IndexerColumnController {

	public UnIndexColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected void update(RobotState state) {
		System.out.println("Running UnIndex Controller");
	}

	@Override
	protected boolean isFinished(RobotState state) {
		return super.isFinished(state);
	}
}
