package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Intake extends SubsystemBase {

    public enum State {
        INTAKE, IDLE
    }

    private static Intake sInstance = new Intake();
    private State mState;
    private ControllerOutput mOutput = new ControllerOutput();

    private Intake() {}

    public static Intake getInstance() {
        return sInstance;
    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        State wantedState = commands.getIntakeWantedState();
        boolean isNewState = mState != wantedState;
        mState = wantedState;
        if (isNewState) {
            switch (wantedState) {
                case INTAKE:
                    // intake

                    break;
                case IDLE:
                    mOutput.setIdle();
                    break;
            }
        }
    }
}
