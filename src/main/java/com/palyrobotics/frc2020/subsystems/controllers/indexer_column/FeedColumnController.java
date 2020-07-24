package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class FeedColumnController extends Indexer.IndexerColumnController {
	private IndexerConfig mConfig = Configs.get(IndexerConfig.class);

	public FeedColumnController(RobotState state) {
		super(state);
	}

	@Override
	protected ControllerOutput update(RobotState robotState) {
		System.out.println("Running Feed Controller");
		mOutputs.setTargetVelocity(500, mConfig.velocityGains);
		return super.update(robotState);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
}
