package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.Robot;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class IndexColumnController extends Indexer.IndexerColumnController {
	private IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	private PIDController mPIDController = new PIDController(mConfig.positionGains.p, mConfig.positionGains.i, mConfig.positionGains.d);
	private double mIndexerWantedPosition;

	public IndexColumnController(RobotState robotState) {
		super(robotState);
		mIndexerWantedPosition = robotState.indexerEncPosition + mConfig.powercellIndexDistance;
	}

	@Override
	protected ControllerOutput update(RobotState robotState) {
		System.out.println("Running Index Controller");
		mPIDController.setPID(mConfig.positionGains.p, mConfig.positionGains.i, mConfig.positionGains.d);
		LiveGraph.add("Target", mIndexerWantedPosition);

		mOutputs.setPercentOutput(MathUtil.clamp(mPIDController.calculate(robotState.indexerEncPosition, mIndexerWantedPosition), -0.3, 0.3));
		return super.update(robotState);
	}

	@Override
	protected boolean isFinished() {
		return super.isFinished();
	}
}
