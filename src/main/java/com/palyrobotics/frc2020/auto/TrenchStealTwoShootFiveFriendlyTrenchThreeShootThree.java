package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import java.util.function.Predicate;

import com.palyrobotics.frc2020.behavior.ParallelRaceRoutine;
import com.palyrobotics.frc2020.behavior.ParallelRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.*;
import com.palyrobotics.frc2020.behavior.routines.superstructure.*;
import com.palyrobotics.frc2020.robot.OperatorInterface;
import com.palyrobotics.frc2020.subsystems.Shooter;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public class TrenchStealTwoShootFiveFriendlyTrenchThreeShootThree implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0, 0, 0);
//
		var getTrenchBalls = new ParallelRaceRoutine(
				new SequentialRoutine(
						new DrivePathRoutine(newWaypointMeters(2.328, 0, 0))
								.setMovement(2.5, 3)),
				new IndexerTimeRoutine(Double.POSITIVE_INFINITY),
				new IntakeBallRoutine(Double.POSITIVE_INFINITY, 1.0));
//
//        var goToShoot = new ParallelRoutine(
//                new DrivePathRoutine(newWaypointMeters(1.05, 3.81, 180)).driveInReverse(),
//                new IndexerTimeRoutine(1.5));
//
//        var shootBalls = new SequentialRoutine(
//                new DriveYawRoutine(180),
//                new ParallelRoutine(
//                        new ShooterVisionRoutine(3.5),
//                        new SequentialRoutine(
//                                new TimedRoutine(0.3),
//                                new IndexerFeedAllRoutine(4.0, false, true))));

		Predicate<Pose2d> readyToShoot = poseMeters -> poseMeters.getTranslation().getY() > 3.2;
		var shootEarly = new SequentialRoutine(
				new DriveParallelPathRoutine(
						new DrivePathRoutine(newWaypointMeters(1.05, 3.81, 165)).driveInReverse(),
						new ParallelRoutine(
								new ShooterCustomVelocityRoutine(2.5, 2000.0, Shooter.HoodState.HIGH),
								new IndexerTimeRoutine(1.5)),
						readyToShoot),
				new ParallelRoutine(
						new ShooterVisionRoutine(3.2),
						new IndexerFeedAllRoutine(3.0, false, true)));

//                new ParallelRaceRoutine(
//                        new DrivePathRoutine(newWaypointMeters(1.05, 3.81, 180)).driveInReverse(),
//                        new ParallelRoutine(
//                                new IndexerTimeRoutine(Double.POSITIVE_INFINITY),
//                                new ShooterCustomVelocityRoutine(Double.POSITIVE_INFINITY, 2000.0, Shooter.HoodState.HIGH)
//                        )
//                ),
//                new ParallelRoutine(
//                        new ShooterVisionRoutine(3.0),
//                        new IndexerFeedAllRoutine(3.0, false, true)));

		var getFriendlyTrenchBalls = new ParallelRaceRoutine(
				new DrivePathRoutine(
						newWaypointMeters(1.8, 6.22, 25),
						newWaypointMeters(4.15, 6.7, 0)),
				new IntakeBallRoutine(Double.POSITIVE_INFINITY),
				new IndexerTimeRoutine(Double.POSITIVE_INFINITY));

		var turnAndShoot = new SequentialRoutine(
				new ParallelRaceRoutine(
						new IndexerTimeRoutine(Double.POSITIVE_INFINITY),
						new ShooterCustomVelocityRoutine(Double.POSITIVE_INFINITY, 2000.0, Shooter.HoodState.HIGH),
						new DriveAlignYawAssistedRoutine(190, OperatorInterface.kOnesTimesZoomAlignButton)),
				new ParallelRoutine(
						new ShooterVisionRoutine(3.0),
						new IndexerFeedAllRoutine(3.0, false, true)));

		return new SequentialRoutine(setInitialOdometry, getTrenchBalls, shootEarly, getFriendlyTrenchBalls, turnAndShoot);
	}
}
