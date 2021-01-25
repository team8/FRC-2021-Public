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

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class BarrelRacing implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
//		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 0.622, 0);
//		List<Pose2d> firstCurve = Arrays.asList(newWaypointMeters(3.2, 2.22, 10), newWaypointMeters(5.86, 2.32, -5));
//		List<Pose2d> loop = Arrays.asList(newWaypointMeters(7.7, 0.74, 0), newWaypointMeters(8.4, 2.052, 120), newWaypointMeters(6.7, 1.532, -105));
//		List<Pose2d> secondCurve = Arrays.asList(newWaypointMeters(5.63, 0.625, -175), newWaypointMeters(3.5, 0.622, 170), newWaypointMeters(1, 2.3, 180));

		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 1.152, 0);
		List<Pose2d> firstCurve = Arrays.asList(newWaypointMeters(2.6, 2.172, 10), newWaypointMeters(5.95, 2.192, -5));
		List<Pose2d> loop = Arrays.asList(newWaypointMeters(7.3, 1.1, -15), newWaypointMeters(8.15, 1.982, 120), newWaypointMeters(6.85, 1.532, -105));
		List<Pose2d> secondCurve = Arrays.asList(newWaypointMeters(6.28, 1.072, -175), newWaypointMeters(3.04, 1.11, 175), newWaypointMeters(1.25, 1.9, 180));

		Log.info(kLoggerTag, String.valueOf(Stream.of(firstCurve, loop, secondCurve).flatMap(List::stream).collect(Collectors.toList())));
		return new SequentialRoutine(setInitialOdometry, new DrivePathRoutine(Stream.of(firstCurve, loop, secondCurve).flatMap(List::stream).collect(Collectors.toList())));
	}
}
