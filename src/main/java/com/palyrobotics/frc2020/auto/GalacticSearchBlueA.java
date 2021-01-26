package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class GalacticSearchBlueA implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		double xPosInit = 0.76;
		double yPosInit = 2.3;
		var setInitialOdometry = new DriveSetOdometryRoutine(0.76, 2.3, 0);
		//will need to split apart for intaking.
		var path = new DrivePathRoutine(
				newWaypointMeters(2.286, 2.286, 0),
				newWaypointMeters(3.81, 1.524, 30),
				newWaypointMeters(4.572, 3.81, 60)
//                newWaypointMeters(0.3,3.81, -180)
		);
		var returnHome = new DrivePathRoutine(
				newWaypointMeters(8.763, 3.81, 180)).driveInReverse();
		return new SequentialRoutine(setInitialOdometry, path, returnHome);
	}
}
