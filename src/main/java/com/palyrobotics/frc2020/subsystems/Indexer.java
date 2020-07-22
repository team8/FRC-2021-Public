package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;

public class Indexer extends SubsystemBase {
    public enum State {
        INDEX, UN_INDEX, FEED, REVERSE_FEED, IDLE
    }

    public abstract static class IndexerColumnController {
        protected ControllerOutput mOutputs = new ControllerOutput();

        public final ControllerOutput update(@ReadOnly Commands commands, @ReadOnly RobotState robotState) {
            return mOutputs;
        }

        public boolean checkFinished() { return false; }
    }

    private static final Indexer sInstance = new Indexer();
    private static final IndexerConfig mConfig = Configs.get(IndexerConfig.class);
    private static State mActiveState = State.IDLE;
    private static ControllerOutput mIndexerColumnOutput = new ControllerOutput();
    private static ControllerOutput mRightVTalonOutput = new ControllerOutput(),
                                    mLeftVTalonOutput = new ControllerOutput();



    public static Indexer getInstance() {
        return sInstance;
    }

    @Override
    public void update(Commands commands, RobotState state) {
//        boolean isNewState = commands.indexerWantedState != mActiveState;
    }

    public ControllerOutput getRightVTalonOutput() {
        return mRightVTalonOutput;
    }

    public ControllerOutput getLeftVTalonOutput() {
        return mLeftVTalonOutput;
    }

    public ControllerOutput getIndexerColumnOutput() {
        return mIndexerColumnOutput;
    }
}
