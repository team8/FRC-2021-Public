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
		var setInitialOdometry = new DriveSetOdometryRoutine(1.21, 0.662, 20);
		List<Pose2d> firstCurve = Arrays.asList(newWaypointMeters(2.3, 1.282, 30), newWaypointMeters(2.58, 1.742, 45), newWaypointMeters(4.56, 2.832, 0), newWaypointMeters(5.9, 2.492, -15), newWaypointMeters(6.7, 1.532, -75));
		List<Pose2d> loop = Arrays.asList(newWaypointMeters(7.18, 0.802, -20), newWaypointMeters(8.4, 0.872, 20), newWaypointMeters(8.75, 1.612, 90), newWaypointMeters(8.32, 2.372, 135), newWaypointMeters(7.27, 2.402, -135), newWaypointMeters(6.7, 1.532, -105));
		List<Pose2d> secondCurve = Arrays.asList(newWaypointMeters(6.03, 0.722, -165), newWaypointMeters(3.74, 0.622, 170), newWaypointMeters(2.27, 1.552, 135), newWaypointMeters(1.76, 2.172, 160), newWaypointMeters(0.9, 2.442, 180));
		Log.info(kLoggerTag, String.valueOf(Stream.of(firstCurve, loop, secondCurve).flatMap(List::stream).collect(Collectors.toList())));
		return new SequentialRoutine(setInitialOdometry, new DrivePathRoutine(Stream.of(firstCurve, loop, secondCurve).flatMap(List::stream).collect(Collectors.toList())));
	}
}
