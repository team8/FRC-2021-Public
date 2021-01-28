package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class Bounce implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 2.27, 0);
		var firstBall = new DrivePathRoutine(newWaypointMeters(2.3, 3.762, 90))
				.setMovement(5, 3);
		var secondBall = new DrivePathRoutine(newWaypointMeters(3.24, 1.29, 140),
				newWaypointMeters(4.6, 3.762, -90))
						.setMovement(5, 3).driveInReverse();
		var thirdBall = new DrivePathRoutine(newWaypointMeters(5.8, 1.0, 0),
				newWaypointMeters(6.85, 3.672, 90))
						.setMovement(5, 3);
		var finishZone = new DrivePathRoutine(newWaypointMeters(8.05, 2.23, 170))
				.setMovement(5, 3).driveInReverse();
		return new SequentialRoutine(setInitialOdometry, firstBall, secondBall, thirdBall, finishZone);
	}
}
