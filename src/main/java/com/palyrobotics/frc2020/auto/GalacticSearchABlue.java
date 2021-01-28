package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class GalacticSearchABlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		double xPosInit = 0.76;
		double yPosInit = 2.3;
		var setInitialOdometry = new DriveSetOdometryRoutine(0.76, 0.76, 0);
		//will need to split apart for intaking.
		var path = new DrivePathRoutine(
				newWaypointMeters(4.572, 0.76, 0),
				newWaypointMeters(5.334, 3.048, 60),
				newWaypointMeters(6.858, 2.286, -60));
		var returnHome = new DrivePathRoutine(
				newWaypointMeters(8.763, 2.286, 0));
		return new SequentialRoutine(setInitialOdometry, path, returnHome);
	}
}
