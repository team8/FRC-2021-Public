package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.SpinnerConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Spinner extends SubsystemBase {

	public enum State {
		SPIN_LEFT, SPIN_RIGHT, IDLE
	}

	private static final Spinner sInstance = new Spinner();
	private final ControllerOutput mOutput = new ControllerOutput();
	private boolean mSolenoidOutput;
	private final SpinnerConfig mConfig = Configs.get(SpinnerConfig.class);

	private Spinner() {
	}

	public static Spinner getInstance() {
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
		switch (commands.spinnerWantedState) {
			case SPIN_LEFT:
				if (!state.spinnerTransitioning && state.spinnerExtended) {
					mOutput.setPercentOutput(-mConfig.spinnerPercentOutput);
				} else {
					mOutput.setIdle();
				}
				mSolenoidOutput = true;
				break;
			case SPIN_RIGHT:
				if (!state.spinnerTransitioning && state.spinnerExtended) {
					mOutput.setPercentOutput(mConfig.spinnerPercentOutput);
				} else {
					mOutput.setIdle();
				}
				mSolenoidOutput = true;
				break;
			case IDLE:
				mOutput.setIdle();
				mSolenoidOutput = false;
				break;
		}
	}
}
