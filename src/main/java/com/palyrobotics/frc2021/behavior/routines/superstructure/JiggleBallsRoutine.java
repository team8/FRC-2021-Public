package com.palyrobotics.frc2021.behavior.routines.superstructure;

import java.util.Set;

import com.palyrobotics.frc2021.behavior.RoutineBase;
import com.palyrobotics.frc2021.robot.ReadOnly;
import com.palyrobotics.frc2021.robot.RobotState;
import com.palyrobotics.frc2021.subsystems.SubsystemBase;

public class JiggleBallsRoutine extends RoutineBase {

	@Override
	public boolean checkFinished(@ReadOnly RobotState state) {
		return false;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return null;
	}
}
