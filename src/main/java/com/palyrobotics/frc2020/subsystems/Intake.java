package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.IntakeConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Intake extends SubsystemBase {

	public enum State {
		INTAKE, IDLE
	}

	private static Intake sInstance = new Intake();
	private ControllerOutput mOutput = new ControllerOutput();
	private boolean mSolenoidOutput;
	private final IntakeConfig mConfig = Configs.get(IntakeConfig.class);

	private Intake() {
	}

	public static Intake getInstance() {
		return sInstance;
	}

	public ControllerOutput getOutput() {
		return mOutput;
	}

	public boolean getSolenoidOutput() {
		return mSolenoidOutput;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		switch (commands.intakeWantedState) {
			case INTAKE:
				mSolenoidOutput = true;
				if (!state.intakeTransitioning) {
					mOutput.setPercentOutput(mConfig.intakeOutput);
				}
				break;
			case IDLE:
				mOutput.setIdle();
				mSolenoidOutput = false;
				break;
		}
	}
}
