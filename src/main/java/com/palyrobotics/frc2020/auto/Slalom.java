package com.palyrobotics.frc2020.auto;

import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.robot.AutoSelector;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.spline.PoseWithCurvature;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.palyrobotics.frc2020.behavior.RoutineManager.kLoggerTag;
import static com.palyrobotics.frc2020.util.Util.newWaypointInches;
import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

public class Slalom implements AutoBase {
    @Override
    public RoutineBase getRoutine() {
        var setInitialOdometry = new DriveSetOdometryRoutine(1.46, 2.162, 10);
        List<Pose2d> firstLoop = Arrays.asList(newWaypointMeters(2.83, 2.412, 10), newWaypointMeters(4.03, 2.132, -30), newWaypointMeters(3.93, 0.942, -180), newWaypointMeters(3.88, 2.04, 10), newWaypointMeters(5.73, 2.312, 15));
        List<Pose2d> secondLoop = Arrays.asList(newWaypointMeters(6.7, 3.152, 90), newWaypointMeters(6.35, 3.552, 170), newWaypointMeters(6.08, 3.572, 180), newWaypointMeters(5.59, 3.062, -90), newWaypointMeters(6.48, 1.582, -45));
        List<Pose2d> thirdLoop = Arrays.asList(newWaypointMeters(7.79, 1.062, 10), newWaypointMeters(8.28, 1.58, 90), newWaypointMeters(7.38, 2.252, 180), newWaypointMeters(5.586, 2.262, 160), newWaypointMeters(4.21, 2.702, 180), newWaypointMeters(1.34, 2.662, 180));
        Log.info(kLoggerTag, String.valueOf(Stream.of(firstLoop, secondLoop).flatMap(List::stream).collect(Collectors.toList())));
        return new SequentialRoutine(setInitialOdometry, new DrivePathRoutine(Stream.of(firstLoop, secondLoop, thirdLoop).flatMap(List::stream).collect(Collectors.toList())));
//        new DrivePathRoutine(setInitialOdometry, firstLoop, secondLoop);
    }
}
