package com.palyrobotics.frc2020.behavior.routines.spinner;

import java.util.Set;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Spinner;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import edu.wpi.first.wpilibj.util.Color;

public class PositionControlRoutine extends RoutineBase {

	private Color goalColor;

	@Override
	public void start(Commands commands, @ReadOnly RobotState state) {
		commands.spinnerWantedState = Spinner.State.SPINNING;
		goalColor = state.spinnerPositionControlColor;
	}

	@Override
	public boolean checkFinished(RobotState state) {
		return goalColor == state.spinnerDetectedColor;
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		if (state.spinnerDetectedColor != goalColor) {
			commands.spinnerWantedState = Spinner.State.SPINNING;
		}
		else {
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
}