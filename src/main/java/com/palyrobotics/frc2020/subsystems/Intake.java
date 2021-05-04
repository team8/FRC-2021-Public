package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;

public class Intake extends SubsystemBase {

    public enum State {
        STOWED, ACTIVE
    }

    private static Intake sInstance;
    private State mState;

    private Intake() {}

    public static Intake getInstance() {
        if(sInstance == null) sInstance = new Intake();
        return sInstance;
    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        State wantedState = commands.getIntakeWantedState();
        boolean isNewState = mState != wantedState;
        mState = wantedState;

        if(isNewState)
            // Do stuff
            ;
    }

}
