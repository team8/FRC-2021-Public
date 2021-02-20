package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.function.Predicate;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveParallelPathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeLowerRoutine;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.util.Units;

public class GalacticSearchARed implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		double xPosInit = 0.76;
		double yPosInit = 2.3;

		Predicate<Pose2d> nearFirstBall = poseMeters -> poseMeters.getTranslation().getX() > Units.inchesToMeters(50.0);
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 2.3, 0);
		var path = new DrivePathRoutine(
				newWaypointMeters(2.286, 2.286, 0),
				newWaypointMeters(3.7, 1.524, 15),
				newWaypointMeters(4.572, 3.65, 60)).endingVelocity(2);
		var pathAndIntake = new DriveParallelPathRoutine(path, new SequentialRoutine(new IntakeLowerRoutine(), new IntakeBallRoutine(4.0)), nearFirstBall);
		var returnHome = new DrivePathRoutine(
				newWaypointMeters(8.2, 4.2, 0)).startingVelocity(2);
		return new SequentialRoutine(setInitialOdometry, pathAndIntake, returnHome);
	}
}
