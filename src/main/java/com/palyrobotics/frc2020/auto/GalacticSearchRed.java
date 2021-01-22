package com.palyrobotics.frc2020.auto;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

public class GalacticSearchRed implements AutoBase {
    @Override
    public RoutineBase getRoutine() {
        double xPosInit = 0.76;
        double yPosInit = 2.3;
        var setInitialOdometry = new DriveSetOdometryRoutine(0.76, 2.3, 0);
        var firstPowercell = new DrivePathRoutine(newWaypointMeters(5, 2.3, 30));
        return new SequentialRoutine(setInitialOdometry, firstPowercell);
    }
}
