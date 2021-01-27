package com.palyrobotics.frc2020.auto;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.vision.Limelight;
import com.palyrobotics.frc2020.vision.LimelightControlMode;

public class GalacticSearchA implements AutoBase {

	private static final double maxAreaBlue = 10; // TODO: get an actual value
	private static final int pipeline = -1; // TODO: get another value

	@Override
	public RoutineBase getRoutine() {
		GalacticSearchARed galacticSearchBlue = new GalacticSearchARed();
		GalacticSearchBRed galacticSearchRed = new GalacticSearchBRed();

		Limelight.getInstance().setPipeline(pipeline);
		Limelight.getInstance().setCamMode(LimelightControlMode.CamMode.DRIVER); // we don't want the LED's
		while (true) {
			if (!Limelight.getInstance().isTargetFound()) {
				continue;
			}

			// If the ball found is a certain angle to the right then choose one routine, otherwise choose the other routine
			// these may need to be swapped depending on where the balls are located in each TODO: confirm this
			if (Limelight.getInstance().getTargetArea() < maxAreaBlue) {
				return galacticSearchBlue.getRoutine();
			} else {
				return galacticSearchRed.getRoutine();
			}
		}
	}
}
