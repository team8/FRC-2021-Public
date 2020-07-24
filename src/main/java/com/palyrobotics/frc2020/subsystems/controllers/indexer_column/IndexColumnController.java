package com.palyrobotics.frc2020.subsystems.controllers.indexer_column;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.util.dashboard.LiveGraph;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class IndexColumnController extends Indexer.IndexerColumnController {
	private IndexerConfig mConfig = Configs.get(IndexerConfig.class);
	private PIDController mMasterSparkPIDController = new PIDController(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
	private PIDController mSlaveSparkPIDController = new PIDController(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);
	private double mMasterSparkEncWantedPosition, mSlaveSparkEncWantedPosition;

	public IndexColumnController(RobotState robotState) {
		super(robotState);
		mMasterSparkEncWantedPosition = robotState.indexerMasterEncPosition + mConfig.powercellIndexDistance;
		mSlaveSparkEncWantedPosition = robotState.indexerSlaveEncPosition + mConfig.powercellIndexDistance;
	}

	@Override
	protected void update(RobotState robotState) {
		System.out.println("Running Index Controller");
		mMasterSparkPIDController.setPID(mConfig.masterSparkPositionGains.p, mConfig.masterSparkPositionGains.i, mConfig.masterSparkPositionGains.d);
		mSlaveSparkPIDController.setPID(mConfig.slaveSparkPositionGains.p, mConfig.slaveSparkPositionGains.i, mConfig.slaveSparkPositionGains.d);
		LiveGraph.add("MasterTarget", mMasterSparkEncWantedPosition);
		LiveGraph.add("SlaveTarget", mSlaveSparkEncWantedPosition);
		mMasterSparkOutput.setPercentOutput(MathUtil.clamp(mMasterSparkPIDController.calculate(robotState.indexerMasterEncPosition, mMasterSparkEncWantedPosition), -0.3, 0.3));
		mSlaveSparkOutput.setPercentOutput(MathUtil.clamp(mSlaveSparkPIDController.calculate(robotState.indexerSlaveEncPosition, mSlaveSparkEncWantedPosition), -0.3, 0.3));
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

