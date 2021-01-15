package com.palyrobotics.frc2021.auto;

import com.palyrobotics.frc2021.behavior.RoutineBase;
import com.palyrobotics.frc2021.util.Util;

public interface AutoBase {

	RoutineBase getRoutine();

	default String getName() {
		return Util.classToJsonName(getClass());
	}

}
