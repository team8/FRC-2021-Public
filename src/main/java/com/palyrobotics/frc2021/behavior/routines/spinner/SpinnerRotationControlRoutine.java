package com.palyrobotics.frc2021.behavior.routines.spinner;

import java.util.Set;

import com.palyrobotics.frc2021.behavior.RoutineBase;
import com.palyrobotics.frc2021.config.subsystem.SpinnerConfig;
import com.palyrobotics.frc2021.robot.Commands;
import com.palyrobotics.frc2021.robot.ReadOnly;
import com.palyrobotics.frc2021.robot.RobotState;
import com.palyrobotics.frc2021.subsystems.Spinner;
import com.palyrobotics.frc2021.subsystems.SubsystemBase;
import com.palyrobotics.frc2021.util.config.Configs;

public class SpinnerRotationControlRoutine extends RoutineBase {

	private SpinnerConfig mConfig = Configs.get(SpinnerConfig.class);
	private String mPreviousColor;
	private int mColorChangeCounter = 0;

	@Override
	protected void start(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedPercentOutput = Configs.get(SpinnerConfig.class).rotationControlPercentOutput;
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		mColorChangeCounter = !state.closestColorString.equals(mPreviousColor) ? mColorChangeCounter + 1 : mColorChangeCounter;
		commands.spinnerWantedState = Spinner.State.ROTATING_RIGHT;
		mPreviousColor = state.closestColorString;
	}

	@Override
	public boolean checkFinished(@ReadOnly RobotState state) {
		return mColorChangeCounter >= mConfig.rotationControlColorChangeRequirementCount;
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
