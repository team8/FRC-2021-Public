package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.util.config.Configs;

public class ForwardTesting implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		DriveConfig config = Configs.get(DriveConfig.class);

		var setInitialOdometry = new DriveSetOdometryRoutine(0, 0, 0);
		var path = new DrivePathRoutine(newWaypointMeters(5, 0, 0)).setMovement(config.maxAutoVelocity, config.maxAutoAcceleration);

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
