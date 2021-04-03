package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class Slalom implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0.9, 0.6, 15);
		var path = new DrivePathRoutine(
				/** -=-=First Curve=-=- **/
				newWaypointMeters(3.35, 2.15, 5),
				newWaypointMeters(5.72, 2.15, 0),

				/** -=-=-=-Loop-=-=-=- **/
				// "longer" turn
				newWaypointMeters(7.72, 0.86, 0),
				newWaypointMeters(7.98, 2.15, 180),
				newWaypointMeters(6.93, 1.59, -110),
				// tighter turn, works on sim but is super tight
				// in lab, turn goes wide, looks slower than the longer turn
//				newWaypointMeters(7.12, 1.18, -55),
//				newWaypointMeters(8.16, 1.5, 90),
//				newWaypointMeters(7, 1.8, -110),

				/** -=-Second Curve-=- **/
				newWaypointMeters(5.8, 0.78, -175),
				newWaypointMeters(3.5, 0.78, 175),
				newWaypointMeters(1.4, 2.0, 165));

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
