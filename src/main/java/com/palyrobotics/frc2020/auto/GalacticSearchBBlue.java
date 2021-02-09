package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.function.Predicate;

import com.palyrobotics.frc2020.behavior.ParallelRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveParallelPathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeLowerRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeStowRoutine;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.util.Units;

public class GalacticSearchBBlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		Predicate<Pose2d> nearFirstBall = poseMeters -> poseMeters.getTranslation().getX() > Units.inchesToMeters(50.0);
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 0.4, 0);
		var path = new DrivePathRoutine(
				newWaypointMeters(4.5, 1.5, 65),
				newWaypointMeters(6.09, 3.07, 0),
				newWaypointMeters(7.8, 1.7, 0)).endingVelocity(2);
		var finishZone = new ParallelRoutine(new DrivePathRoutine(newWaypointMeters(8.0, 1.7, 0)).startingVelocity(2).endingVelocity(1), new IntakeStowRoutine());
		var pathAndIntake = new DriveParallelPathRoutine(path, new SequentialRoutine(new IntakeLowerRoutine(), new IntakeBallRoutine(4.0)), nearFirstBall);

		return new SequentialRoutine(setInitialOdometry, pathAndIntake, finishZone);
	}
}
