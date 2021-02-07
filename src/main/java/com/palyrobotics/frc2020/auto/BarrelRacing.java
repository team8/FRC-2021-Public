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
//		var setInitialOdometry = new DriveSetOdometryRoutine(1.46, 2.162, 10);
//		List<Pose2d> firstLoop = Arrays.asList(newWaypointMeters(2.83, 2.412, 10), newWaypointMeters(4.03, 2.132, -30), newWaypointMeters(3.93, 0.7, -180), newWaypointMeters(3.88, 2.04, 10), newWaypointMeters(5.73, 2.312, 15));
//		List<Pose2d> secondLoop = Arrays.asList(newWaypointMeters(6.89, 3.342, 100), newWaypointMeters(6.15, 3.862, -170), newWaypointMeters(5.57, 3.032, -70));
//		List<Pose2d> thirdLoop = Arrays.asList(newWaypointMeters(6.85, 1.23, -20), newWaypointMeters(8.22, 1.522, 90), newWaypointMeters(7.38, 2.252, 180), newWaypointMeters(5.586, 2.262, 180), newWaypointMeters(0.5, 2.662, 180));
//		return new SequentialRoutine(setInitialOdometry, new DrivePathRoutine(firstLoop).endingVelocity(mConfig.pathVelocityMetersPerSecond), new DrivePathRoutine(secondLoop).endingVelocity(mConfig.pathVelocityMetersPerSecond).startingVelocity(mConfig.pathVelocityMetersPerSecond), new DrivePathRoutine(thirdLoop).setMovement(mConfig.pathVelocityMetersPerSecond * 1.4, mConfig.pathAccelerationMetersPerSecondSquared * 1.3).startingVelocity(mConfig.pathVelocityMetersPerSecond));

		var setInitialOdometry = new DriveSetOdometryRoutine(1.14, 2.23, 0);
//		var circularWaypoints = new DrivePathRoutine().waypoints(Util.circlePathGenerator(new Translation2d(3, 3), 2, 0, 180));
		var loops = new DrivePathRoutine(
				/** First loop **/
				newWaypointMeters(4.15, 2.23, -15),
				newWaypointMeters(3.72, 0.6, 170),
				newWaypointMeters(3.15, 2.05, 30),
				/** Second loop **/
				newWaypointMeters(6.6, 2.35, 30),
				newWaypointMeters(5.6, 3.9, -160),
				/** Third loop **/
//				newWaypointMeters(6.58, 1, -10),
				newWaypointMeters(6.35, 1.25, -35),
//				newWaypointMeters(8.19, 1.062, 30),
				newWaypointMeters(8.55, 1.25, 55),
				newWaypointMeters(7.8, 2.5, 180)).endingVelocity(mConfig.straightPathVelocityMetersPerSecond);
		var toStart = new DrivePathRoutine(
				newWaypointMeters(1.2, 2.5, 180)).startingVelocity(mConfig.straightPathVelocityMetersPerSecond).setMovement(mConfig.straightPathVelocityMetersPerSecond, mConfig.straightPathAccelerationMetersPerSecondSquared);
		return new SequentialRoutine(setInitialOdometry, loops, toStart);
	}
}
