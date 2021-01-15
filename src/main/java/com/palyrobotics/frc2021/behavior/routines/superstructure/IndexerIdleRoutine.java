package com.palyrobotics.frc2021.behavior.routines.superstructure;

import java.util.Set;

import com.palyrobotics.frc2021.behavior.OneUpdateRoutineBase;
import com.palyrobotics.frc2021.robot.Commands;
import com.palyrobotics.frc2021.robot.ReadOnly;
import com.palyrobotics.frc2021.robot.RobotState;
import com.palyrobotics.frc2021.subsystems.Indexer;
import com.palyrobotics.frc2021.subsystems.SubsystemBase;

public class IndexerIdleRoutine extends OneUpdateRoutineBase {

	@Override
	protected void updateOnce(Commands commands, @ReadOnly RobotState state) {
		commands.indexerColumnWantedState = Indexer.ColumnState.IDLE;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return Set.of(mIndexer);
	}
}
