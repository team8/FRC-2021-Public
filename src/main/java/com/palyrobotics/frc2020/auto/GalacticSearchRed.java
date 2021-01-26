package com.palyrobotics.frc2020.auto;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.vision.Limelight;

public class GalacticSearchRed implements AutoBase {

    private static final double maxYawA = 10; // TODO: get an actual value

    @Override
    public RoutineBase getRoutine() {
        GalacticSearchRedA galacticSearchRedA = new GalacticSearchRedA();
        GalacticSearchRedB galacticSearchRedB = new GalacticSearchRedB();

        while (true) {
            if (!Limelight.getInstance().isTargetFound()) {
                continue;
            }

            if (Limelight.getInstance().getYawToTarget() < maxYawA) {
                return galacticSearchRedA.getRoutine();
            } else {
                return galacticSearchRedB.getRoutine();
            }
        }
    }
}
