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
				newWaypointMeters(2.3, 3.45, 90));
		var secondBall = new DrivePathRoutine(
				newWaypointMeters(3.07, 1.45, 120),
				newWaypointMeters(4.45, 1.23, -110),
				newWaypointMeters(4.6, 3.45, -90)).driveInReverse();
		var thirdBall = new DrivePathRoutine(
				newWaypointMeters(5.8, 0.78, 0),
				newWaypointMeters(6.85, 3.45, 90));
		var finishZone = new DrivePathRoutine(
				newWaypointMeters(8.05, 2.23, 170)).driveInReverse();
		return new SequentialRoutine(setInitialOdometry, firstBall, secondBall, thirdBall, finishZone);
	}
}
