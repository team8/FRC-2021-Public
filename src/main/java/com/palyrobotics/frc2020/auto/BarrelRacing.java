package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class BarrelRacing implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(1.0, 0.8, 0);
		// Making one large routine since there shouldn't be any stops along the way, just one large motion
		var path = new DrivePathRoutine(
				/** -=-First Curve-=- **/
				newWaypointMeters(2.8, 2.1, 35),
//				newWaypointMeters(5.9, 1.85, -15),
//				newWaypointMeters(6.25, 2.3, -50),
				newWaypointMeters(6.5, 1.9, -50),

				/** -=-=-=-Loop-=-=-=- **/
//				newWaypointMeters(7.07, 0.85, -35),
//				newWaypointMeters(7.30, 0.73, -20),
//				newWaypointMeters(7.58, 0.69, 0),
				newWaypointMeters(8.09, 0.73, 10),
//				newWaypointMeters(8.57, 1.05, 75),
//				newWaypointMeters(8.36, 0.62, 20),
//				newWaypointMeters(8.39, 2.27, 135),
				newWaypointMeters(8.18, 2.43, 160),
//				newWaypointMeters(7.02, 2.12, -120),
				newWaypointMeters(6.75, 1.66, -110),

				/** -=-Second Curve-=- **/
//				newWaypointMeters(6.3, 0.8, -160),
				newWaypointMeters(5.66, 0.62, -165),
				newWaypointMeters(2.83, 0.87, 138),
				newWaypointMeters(1.25, 2.15, 180));

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
