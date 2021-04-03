package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.function.Predicate;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.TimedRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveParallelPathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeLowerRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeStowRoutine;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.util.Units;

public class GalacticSearchABlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		Predicate<Pose2d> nearFirstBall = poseMeters -> poseMeters.getTranslation().getX() > Units.inchesToMeters(60.0);
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 0.4, 0);
		var lowerIntake = new SequentialRoutine(new IntakeLowerRoutine(), new TimedRoutine(1));
		var path = new DrivePathRoutine(
				newWaypointMeters(4.52, 0.8, 50),
				newWaypointMeters(5.28, 2.8, 65),
				newWaypointMeters(6.858, 2.45, -20),
				newWaypointMeters(8.2, 1.8, -20));
		var pathAndIntake = new DriveParallelPathRoutine(path, new SequentialRoutine(new IntakeBallRoutine(4.5), new IntakeStowRoutine()), nearFirstBall);

		return new SequentialRoutine(setInitialOdometry, lowerIntake, pathAndIntake);
	}
}
