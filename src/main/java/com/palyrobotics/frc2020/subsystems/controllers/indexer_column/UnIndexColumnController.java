package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class UnIndexColumnController extends Indexer.IndexerColumnController {

	public UnIndexColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected ControllerOutput update(RobotState robotState) {
		System.out.println("Running UnIndex Controller");
		return super.update(robotState);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
}
