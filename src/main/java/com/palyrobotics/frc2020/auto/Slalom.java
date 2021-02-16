package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.function.Predicate;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class Slalom implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0.9, 0.6, 15);
		var path = new DrivePathRoutine(
				newWaypointMeters(3.65, 2.2, 0),
				newWaypointMeters(5.85, 2.1, -5),

				/** -=-=-=-Loop-=-=-=- **/
				// "longer" turn
				newWaypointMeters(7.62, 0.85, -10),
				newWaypointMeters(7.7, 2.15, 180),
				newWaypointMeters(7.03, 1.59, -100),

				// tighter turn, works on sim but is super tight
				// in lab, turn goes wide, looks slower than the longer turn
//				newWaypointMeters(7.12, 1.18, -55),
//				newWaypointMeters(8.16, 1.5, 90),
//				newWaypointMeters(7, 1.8, -110),

				/** -=-Second Curve-=- **/
				newWaypointMeters(6, 0.8, -170),
				newWaypointMeters(3.35, 0.85, 165),
				newWaypointMeters(1.4, 2.4, 140));

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
