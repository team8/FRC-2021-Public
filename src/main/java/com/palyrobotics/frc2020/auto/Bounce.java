package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class Bounce implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(1.0, 2.35, 0);
		var firstBall = new DrivePathRoutine(
				newWaypointMeters(2.2, 3.38, 90));
		var secondBall = new DrivePathRoutine(
				newWaypointMeters(3.0, 1.65, 120),
				newWaypointMeters(4.35, 1.45, -110),
				newWaypointMeters(4.48, 3.57, -90)).driveInReverse();
		var thirdBall = new DrivePathRoutine(
				newWaypointMeters(5.55, 1.04, 0),
				newWaypointMeters(6.75, 3.65, 90));
		var finishZone = new DrivePathRoutine(
				newWaypointMeters(8.05, 2.5, 160)).driveInReverse();

		// A lot of drift towards neg y, not sure why
		return new SequentialRoutine(setInitialOdometry, firstBall, secondBall, thirdBall, finishZone);
	}
}
