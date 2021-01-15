package com.palyrobotics.frc2021.subsystems;

import com.palyrobotics.frc2021.robot.Commands;
import com.palyrobotics.frc2021.robot.ReadOnly;
import com.palyrobotics.frc2021.robot.RobotState;
import com.palyrobotics.frc2021.util.Util;

public abstract class SubsystemBase {

	private final String mName;

	protected SubsystemBase() {
		mName = Util.classToJsonName(getClass());
	}

	public abstract void update(@ReadOnly Commands commands, @ReadOnly RobotState state);

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return mName;
	}
}
