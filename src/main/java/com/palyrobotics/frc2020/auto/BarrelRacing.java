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

		var setInitialOdometry = new DriveSetOdometryRoutine(0.9, 2.2, 20);
		var loops = new DrivePathRoutine(
				/** First loop **/
				newWaypointMeters(4.1, 2.1, -50),
				newWaypointMeters(3.3, 0.98, 145),
				newWaypointMeters(3.98, 2.25, 10),

				/** Second loop **/
				newWaypointMeters(6.65, 2.9, 75),
				newWaypointMeters(5.45, 3.3, -105),

				/** Third loop **/
				newWaypointMeters(6.9, 0.95, -20),
				newWaypointMeters(8.02, 2.2, 170),

				/** Finish **/
				newWaypointMeters(6.4, 2.3, 180),
				newWaypointMeters(1.2, 2.3, 180));
//
		return new SequentialRoutine(setInitialOdometry, loops);
	}
}
