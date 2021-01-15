package com.palyrobotics.frc2021.behavior.routines.superstructure;

import com.palyrobotics.frc2021.behavior.TimeoutRoutineBase;
import com.palyrobotics.frc2021.robot.Commands;
import com.palyrobotics.frc2021.robot.ReadOnly;
import com.palyrobotics.frc2021.robot.RobotState;

public class ShooterVisionRoutine extends TimeoutRoutineBase {

	public ShooterVisionRoutine(double timeoutSeconds) {
		super(timeoutSeconds);
	}

	@Override
	public void update(Commands commands, @ReadOnly RobotState state) {
		commands.setShooterVisionAssisted(0);
	}

	@Override
	public void stop(Commands commands, @ReadOnly RobotState state) {
		commands.setShooterIdle();
	}

	@Override
	public boolean checkIfFinishedEarly(@ReadOnly RobotState state) {
		return false;
	}
}
