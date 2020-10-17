package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypoint;

import com.palyrobotics.frc2020.behavior.ParallelRaceRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;

/**
 * Start by facing and aligning the center of the intake to the middle of the two balls of the
 * opponent's trench. Pull the robot back until just the back bumper covers the initiation line. The
 * left ball should be centered in the intake from this configuration.
 */
public class TrenchStealTwoShootFiveRendezvousTwo implements AutoBase {

    @Override
    public RoutineBase getRoutine() {
        var setInitialOdometry = new DriveSetOdometryRoutine(0, 0, 0);
        var getTrenchBalls = new ParallelRaceRoutine(
                new SequentialRoutine(
                        new DrivePathRoutine(newWaypoint(92, 0, 0))
                                .setMovement(1.5, 2.4),
                        new DriveYawRoutine(15.0)));

        var goToShoot = new DrivePathRoutine(newWaypoint(60, 70, 150))
                        .setMovement(1.5, 2.4)
                        .driveInReverse();


        var turnAndIntake = new ParallelRaceRoutine(
                new SequentialRoutine(
                        new DrivePathRoutine(newWaypoint(77, 113, 15.0))
                                .setMovement(2.0, 2.0),
                        new DriveYawRoutine(20.0)));
//        var backupAndShoot = new SequentialRoutine(
//                new DrivePathRoutine(newWaypoint(40.0, 113.0, 150.0))
//                        .driveInReverse(),
//                new ParallelRoutine(
//                        new DriveAlignRoutine(0),
//                        new ShooterVisionRoutine(6),
//                        new SequentialRoutine(
//                                new TimedRoutine(1.0),
//                                new SequentialRoutine(
//                                        new IndexerFeedAllRoutine(0.4, false, true),
//                                        new IndexerFeedAllRoutine(4.6, false, true)
//                                )))
//        );
//        return new SequentialRoutine(setInitialOdometry, getTrenchBalls, goToShoot, shootBalls, turnAndIntake, backupAndShoot);
        return new SequentialRoutine(setInitialOdometry, getTrenchBalls, goToShoot, turnAndIntake);
    }
}

