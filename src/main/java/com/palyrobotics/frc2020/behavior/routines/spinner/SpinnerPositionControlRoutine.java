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

	private String goalColor;
	private Spinner.State direction;

	@Override
	public void start(Commands commands, @ReadOnly RobotState state) {
		goalColor = state.gameData;
		direction = spinningDirection(state.spinnerDetectedColor, goalColor);
		commands.spinnerWantedState = direction;
	}

	@Override
	public boolean checkFinished(RobotState state) {
		return goalColor.equals(state.spinnerDetectedColor);
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		if (!state.spinnerDetectedColor.equals(goalColor)) {
			commands.spinnerWantedState = direction;
		} else {
			commands.spinnerWantedState = Spinner.State.IDLE;
		}
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
			if (Math.abs(distance) > SpinnerConstants.colorOrder.size() / 2) return Spinner.State.SPINNING_LEFT;
			else return Spinner.State.SPINNING_RIGHT;
		} else {
			if (distance > SpinnerConstants.colorOrder.size() / 2) return Spinner.State.SPINNING_RIGHT;
			else return Spinner.State.SPINNING_LEFT;
		}
	}
}
