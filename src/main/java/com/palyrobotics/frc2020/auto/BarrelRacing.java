package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

public class BarrelRacing implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 1.05, 10);
//		var firstCurve = new DrivePathRoutine(
//				newWaypointMeters(2.6, 2.172, 10),
//				newWaypointMeters(5.95, 2.192, -5)).setMovement(3.1, 0.7);
//		var loop = new DrivePathRoutine(
//				newWaypointMeters(7.3, 1.1, -15),
//				newWaypointMeters(8.15, 1.982, 120),
//				newWaypointMeters(6.85, 1.532, -105)).setMovement(2, 0.5);
//		var secondCurve = new DrivePathRoutine(
//				newWaypointMeters(6.12, 1.072, -175),
//				newWaypointMeters(3.04, 1.11, 175),
//				newWaypointMeters(1.25, 1.9, 180)).setMovement(3, 0.8);

		// Making one large routine since there shouldn't be any stops along the way, just one large motion
		var path = new DrivePathRoutine(
				/** -=-First Curve-=- **/
				newWaypointMeters(2.6, 2, 25),
				newWaypointMeters(5.9, 2.08, -15),

				/** -=-=-=-Loop-=-=-=- **/
				newWaypointMeters(7.4, 1.1, 0),
				newWaypointMeters(7.85, 1.8, 140),
				newWaypointMeters(6.98, 1.48, -105),
				/* Wide loop, slower */
//				newWaypointMeters(7.4, 0.78, 0),
//				newWaypointMeters(8.2, 1.99, 140),
//				newWaypointMeters(6.95, 1.55, -105),

				/** -=-Second Curve-=- **/
				newWaypointMeters(5.75, 1.072, -175),
				newWaypointMeters(3.04, 1.11, 175),
				newWaypointMeters(1.25, 1.98, 175))
						.setMovement(5.3, 3.2);

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
