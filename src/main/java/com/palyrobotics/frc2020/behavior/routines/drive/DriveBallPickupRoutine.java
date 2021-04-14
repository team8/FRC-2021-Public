package com.palyrobotics.frc2020.behavior.routines.drive;

import java.util.Set;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;

public class DriveBallPickupRoutine extends DriveYawRoutine {

	public DriveBallPickupRoutine(double yawDegrees) {
		super(yawDegrees);
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		if (state.balls.size() > 0) {
			commands.setDriveBallPickup();
		} else {
			commands.setDriveYaw(mTargetYawDegrees);
		}
	}

	@Override
	protected void stop(Commands commands, @ReadOnly RobotState state) {
		commands.visionWanted = false; //TODO: Find an odroid equivalent to this
	}

	@Override
	public boolean checkIfFinishedEarly(@ReadOnly RobotState state) {
		return state.isAligned;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return Set.of(mDrive);
	}
}
