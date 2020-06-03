package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Turret extends SubsystemBase {

	private static Turret sInstance = new Turret();
	private ControllerOutput mOutput = new ControllerOutput();

	private Turret() {

	}

	public static Turret getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {

	}

	public ControllerOutput getOutput() {
		return mOutput;
	}
}
