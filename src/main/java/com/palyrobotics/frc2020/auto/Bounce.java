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
		var firstBall = new DrivePathRoutine(newWaypointMeters(2.3, 3.762, 90));
		var secondBall1 = new DrivePathRoutine(newWaypointMeters(3.0, 0.7, 110)).driveInReverse();
		var secondBall2 = new DrivePathRoutine(newWaypointMeters(4.5, 1.3, 80));
		var secondBall3 = new DrivePathRoutine(newWaypointMeters(4.57, 3.762, 90));
		var thirdBall1 = new DrivePathRoutine(newWaypointMeters(5.0, 1.0, 110)).driveInReverse();
		var thirdBall2 = new DrivePathRoutine(newWaypointMeters(6.63, 1.45, 60));
		var thirdBall3 = new DrivePathRoutine(newWaypointMeters(6.85, 3.762, 90));
		var finishZone = new DrivePathRoutine(newWaypointMeters(8.2, 2.23, 0));
		return new SequentialRoutine(setInitialOdometry, firstBall, secondBall1, secondBall2, secondBall3, thirdBall1, thirdBall2, thirdBall3, finishZone);
	}
}
