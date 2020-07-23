package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.Robot;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class IndexColumnController extends Indexer.IndexerColumnController {
	IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	double initialIndexerEncPosition;

	public IndexColumnController(RobotState robotState) {
		super(robotState);
		initialIndexerEncPosition = robotState.indexerEncPosition;
	}

	@Override
	protected ControllerOutput update(RobotState robotState) {
		System.out.println("Running Index Controller");
		mOutputs.setTargetPosition(initialIndexerEncPosition + mConfig.powercellIndexDistance, mConfig.positionGains);
		return super.update(robotState);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
}
