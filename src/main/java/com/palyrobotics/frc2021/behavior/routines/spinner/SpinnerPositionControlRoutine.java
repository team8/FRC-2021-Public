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

public class SpinnerPositionControlRoutine extends RoutineBase {

	private int mDirectionToGoalColor;

	@Override
	protected void start(Commands commands, @ReadOnly RobotState state) {
		mDirectionToGoalColor = mSpinner.directionToGoalColor(state.closestColorString, state.gameData);
		commands.spinnerWantedPercentOutput = Configs.get(SpinnerConfig.class).positionControlPercentOutput;
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedState = mDirectionToGoalColor < 0 ? Spinner.State.ROTATING_RIGHT : Spinner.State.ROTATING_LEFT;
	}

	@Override
	public boolean checkFinished(@ReadOnly RobotState state) {
		return state.gameData.equals(state.closestColorString);
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
