package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.util.config.Configs;

public class BarrelRacing implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		DriveConfig mConfig = Configs.get(DriveConfig.class);

		var setInitialOdometry = new DriveSetOdometryRoutine(1.0, 2.5, 0);
		var loops = new DrivePathRoutine(
		/** First loop **/
//				newWaypointMeters(4.1, 2.1, -50),

				newWaypointMeters(4.15, 2.0, -50),
				newWaypointMeters(3.15, 0.92, 145),
				newWaypointMeters(3.93, 2.25, 10),

				/** Second loop **/
				newWaypointMeters(6.45, 3.0, 75),
				newWaypointMeters(5.1, 3.65, -105),

				/** Third loop **/
				newWaypointMeters(6.8, 1.67, -20),
				newWaypointMeters(7.95, 2.4, 105),
//				newWaypointMeters(7.9, 2.4, 170),

				/** Finish **/
				newWaypointMeters(6.5, 2.85, 170),
				newWaypointMeters(0.6, 2.85, 180));
		// Centr. Accel is 4 for this auto
//
		return new SequentialRoutine(setInitialOdometry, loops);
	}
}
