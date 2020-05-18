package com.palyrobotics.frc2020.robot;

import java.util.ArrayList;
import java.util.List;

import com.palyrobotics.frc2020.behavior.RoutineBase;

/**
 * Commands represent what we want the robot to be doing.
 */
@SuppressWarnings ("squid:ClassVariableVisibilityCheck")
public class Commands {

	/* Routines */
	public List<RoutineBase> routinesWanted = new ArrayList<>();
	public boolean shouldClearCurrentRoutines;

	public void addWantedRoutines(RoutineBase... wantedRoutines) {
		for (RoutineBase wantedRoutine : wantedRoutines) {
			addWantedRoutine(wantedRoutine);
		}
	}

	public void addWantedRoutine(RoutineBase wantedRoutine) {
		routinesWanted.add(wantedRoutine);
	}

}
