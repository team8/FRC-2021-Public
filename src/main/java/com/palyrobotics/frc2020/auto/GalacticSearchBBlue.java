package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.ParallelRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeLowerRoutine;

public class GalacticSearchBBlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0.5, 2.3, 0);
		var firstBall = new DrivePathRoutine(newWaypointMeters(4.5, 1.5, 30));
		var secondBall = new DrivePathRoutine(newWaypointMeters(6.09, 3.07, 0));
		var thirdBall = new DrivePathRoutine(newWaypointMeters(7.8, 1.5, 0));
		var finishZone = new DrivePathRoutine(newWaypointMeters(8.75, 2.23, 0));
		var path = new SequentialRoutine(firstBall, secondBall, thirdBall);
		var pathAndIntake = new ParallelRoutine(path, new SequentialRoutine(new IntakeLowerRoutine(), new IntakeBallRoutine(6.0)));

		return new SequentialRoutine(setInitialOdometry, pathAndIntake, finishZone);
	}
}
