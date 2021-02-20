package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;

public class Tuning implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var initial = new DriveSetOdometryRoutine(0, 0, 0);
		var test = new DrivePathRoutine(
//				newWaypointMeters(1, 1, 90),
				newWaypointMeters(0, 2, -180));
//				newWaypointMeters(2.5, 0, 0));

		var test2 = new DriveYawRoutine(90);
		return new SequentialRoutine(initial, test);
	}
}
