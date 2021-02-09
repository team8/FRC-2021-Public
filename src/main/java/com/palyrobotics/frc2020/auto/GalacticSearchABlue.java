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

public class GalacticSearchABlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		double xPosInit = 0.76;
		double yPosInit = 2.3;

		Predicate<Pose2d> nearFirstBall = poseMeters -> poseMeters.getTranslation().getX() > Units.inchesToMeters(70.0);
		var setInitialOdometry = new DriveSetOdometryRoutine(1.2, 0.4, 0);
		//will need to split apart for intaking.
		var path = new DrivePathRoutine(
				newWaypointMeters(4.572, 0.8, 50),
				newWaypointMeters(5.334, 2.8, 70),
				newWaypointMeters(6.858, 2.45, -30)).endingVelocity(2);
		var returnHome = new ParallelRoutine(new DrivePathRoutine(
				newWaypointMeters(8.0, 1.8, -30)).startingVelocity(2).endingVelocity(1), new IntakeStowRoutine());
		var pathAndIntake = new DriveParallelPathRoutine(path, new SequentialRoutine(new IntakeLowerRoutine(), new IntakeBallRoutine(4.0)), nearFirstBall);

		return new SequentialRoutine(setInitialOdometry, pathAndIntake, returnHome);
	}
}
