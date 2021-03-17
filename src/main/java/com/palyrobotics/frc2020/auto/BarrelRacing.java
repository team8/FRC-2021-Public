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
				newWaypointMeters(3.2, 0.92, 145),
				newWaypointMeters(3.93, 2.25, 10),

				/** Second loop **/
				newWaypointMeters(6.43, 2.85, 75),
				newWaypointMeters(5.3, 3.33, -105),

				/** Third loop **/
				newWaypointMeters(6.8, 1.3, -20),
				newWaypointMeters(7.9, 2.4, 170),

				/** Finish **/
				newWaypointMeters(6.4, 2.5, 179),
				newWaypointMeters(1.2, 2.5, 180));
		// Centr. Accel is lower (around 3.5) for this auto
//
		return new SequentialRoutine(setInitialOdometry, loops);
	}
}
