package com.palyrobotics.frc2020.auto;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.util.config.Configs;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

public class LoopTesting implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		DriveConfig config = Configs.get(DriveConfig.class);

		var setInitialOdometry = new DriveSetOdometryRoutine(0, 0, 0);
		var goForward = new DrivePathRoutine(newWaypointMeters(0, 15, 0));

		var turn = new DriveYawRoutine(90);

		var wheeeeeeeeeeeee = new DrivePathRoutine(
				newWaypointMeters(-3.5, 12.5, 135),
				newWaypointMeters(-5, 10, -180),
				newWaypointMeters(-3.5, 7.5, -45),
				newWaypointMeters(0, 5, 90)
		).setMovement(config.maxAutoVelocity, config.maxAutoAcceleration);



		return new SequentialRoutine(setInitialOdometry, goForward);
	}
}
