package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypointMeters;

import com.palyrobotics.frc2020.behavior.ParallelRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeLowerRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.IntakeStowRoutine;

public class GalacticSearchBBlue implements AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0.5, 2.3, 0);
		var balls = new DrivePathRoutine(
				newWaypointMeters(4.5, 1.5, 30),
				newWaypointMeters(6.09, 3.07, 0),
				newWaypointMeters(7.8, 1.5, 0));
		var finishZone = new ParallelRoutine(new DrivePathRoutine(newWaypointMeters(8.75, 2.23, 0)), new IntakeStowRoutine());
		var pathAndIntake = new ParallelRoutine(balls, new SequentialRoutine(new IntakeLowerRoutine(), new IntakeBallRoutine(6.0)));

		return new SequentialRoutine(setInitialOdometry, pathAndIntake, finishZone);
	}
}
