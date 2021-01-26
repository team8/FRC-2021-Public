package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class GalacticSearchRedB implements AutoBase {

	@Override
	public RoutineBase getRoutine() {

		var setInitialOdometry = new DriveSetOdometryRoutine(0.76, 3.048, 0);
		//will need to split apart for intaking.
		var path = new DrivePathRoutine(
				newWaypointMeters(2.286, 3.048, 0),
				newWaypointMeters(3.81, 1.524, -40),
				newWaypointMeters(5.334, 3.048, 0));

		var returnHome = new DrivePathRoutine(
				newWaypointMeters(8.763, 3.048, 0));
		return new SequentialRoutine(setInitialOdometry, path, returnHome);
	}
}
