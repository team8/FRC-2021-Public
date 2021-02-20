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
				//With omni wheels, set centripetal accel to 2.3, otherwise it overturns and swerves
				newWaypointMeters(3.65, 2.3, 0),
				newWaypointMeters(5.83, 2.3, 0),

				/** -=-=-=-Loop-=-=-=- **/
				// "longer" turn
				newWaypointMeters(7.72, 0.8, -10),
				newWaypointMeters(7.80, 2.1, 180),
				newWaypointMeters(6.9, 1.59, -100),
				// tighter turn, works on sim but is super tight
				// in lab, turn goes wide, looks slower than the longer turn
//				newWaypointMeters(7.12, 1.18, -55),
//				newWaypointMeters(8.16, 1.5, 90),
//				newWaypointMeters(7, 1.8, -110),

				/** -=-Second Curve-=- **/
				newWaypointMeters(6, 0.72, -170),
				newWaypointMeters(3.6, 0.65, 180),
				newWaypointMeters(1.5, 2.2, 155));

		return new SequentialRoutine(setInitialOdometry, path);
	}
}
