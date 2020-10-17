package com.palyrobotics.frc2020.behavior.routines.spinner;

import java.util.Set;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.config.constants.SpinnerConstants;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Spinner;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;

public class SpinnerPositionControlRoutine extends RoutineBase {

	private String mGoalColor;
	private Spinner.State mDirection;

	@Override
	public void start(Commands commands, @ReadOnly RobotState state) {
		mGoalColor = state.gameData;
		mDirection = spinningDirection(state.spinnerDetectedColor, mGoalColor);
		commands.spinnerWantedState = mDirection;
	}

	@Override
	public boolean checkFinished(RobotState state) {
		return mGoalColor.equals(state.spinnerDetectedColor);
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
	}

	@Override
	protected void stop(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedState = Spinner.State.IDLE;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return Set.of(mSpinner);
	}

	private Spinner.State spinningDirection(String currentColor, String wantedColor) {
		int distance = SpinnerConstants.colorOrder.indexOf(currentColor) - SpinnerConstants.colorOrder.indexOf(wantedColor);
		if (distance < 0) {
			return (Math.abs(distance) > SpinnerConstants.colorOrder.size() / 2) ? Spinner.State.SPIN_LEFT : Spinner.State.SPIN_RIGHT;
		} else {
			return (distance > SpinnerConstants.colorOrder.size() / 2) ? Spinner.State.SPIN_RIGHT : Spinner.State.SPIN_LEFT;
		}
	}
}
