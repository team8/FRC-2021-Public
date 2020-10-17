package com.palyrobotics.frc2020.behavior.routines.spinner;

import java.util.Set;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.config.subsystem.SpinnerConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Spinner;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.config.Configs;

public class SpinnerRotationControlRoutine extends RoutineBase {

	private int mColorsSeenCounter;
	private String mPreviousColor;
	private final SpinnerConfig mConfig = Configs.get(SpinnerConfig.class);

	@Override
	public void start(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedState = Spinner.State.SPIN_RIGHT;
		mColorsSeenCounter = 0;
	}

	@Override
	public boolean checkFinished(RobotState state) {
		return mColorsSeenCounter > mConfig.spinnerRotationColorsPassed;
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		if (!state.spinnerDetectedColor.equals(mPreviousColor)) {
			mColorsSeenCounter++;
		}
		mPreviousColor = state.spinnerDetectedColor;
	}

	@Override
	protected void stop(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedState = Spinner.State.IDLE;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return Set.of(mSpinner);
	}
}
