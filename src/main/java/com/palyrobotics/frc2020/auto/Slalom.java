package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.behavior.RoutineManager.kLoggerTag;
import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.util.config.Configs;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class Slalom implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		DriveConfig mConfig = Configs.get(DriveConfig.class);
		var setInitialOdometry = new DriveSetOdometryRoutine(1.46, 2.162, 10);
		List<Pose2d> firstLoop = Arrays.asList(newWaypointMeters(2.83, 2.412, 10), newWaypointMeters(4.03, 2.132, -30), newWaypointMeters(3.93, 0.942, -180), newWaypointMeters(3.88, 2.04, 10), newWaypointMeters(5.73, 2.312, 15));
		List<Pose2d> secondLoop = Arrays.asList(newWaypointMeters(6.76, 2.93, 45), newWaypointMeters(6.46, 3.572, 170), newWaypointMeters(5.85, 3.461, -160), newWaypointMeters(5.63, 2.822, -70));
		List<Pose2d> thirdLoop = Arrays.asList(newWaypointMeters(6.85, 1.23, -20), newWaypointMeters(8.22, 1.522, 90), newWaypointMeters(7.38, 2.252, 180), newWaypointMeters(5.586, 2.262, 160), newWaypointMeters(4.21, 2.702, 180), newWaypointMeters(1.34, 2.662, 180));
		Log.info(kLoggerTag, String.valueOf(Stream.of(firstLoop, secondLoop).flatMap(List::stream).collect(Collectors.toList())));
		return new SequentialRoutine(setInitialOdometry, new DrivePathRoutine(Stream.of(firstLoop, secondLoop).flatMap(List::stream).collect(Collectors.toList())), new DrivePathRoutine(thirdLoop).startingVelocity(mConfig.pathVelocityMetersPerSecond));
//        new DrivePathRoutine(setInitialOdometry, firstLoop, secondLoop);
	}
}
