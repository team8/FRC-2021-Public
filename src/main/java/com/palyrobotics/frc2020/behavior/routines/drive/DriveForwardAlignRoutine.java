package com.palyrobotics.frc2020.behavior.routines.drive;

import static com.palyrobotics.frc2020.util.Util.getDifferenceInAngleDegrees;

import java.util.Objects;
import java.util.Set;

import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.wpilibj.geometry.Pose2d;

public class DriveForwardAlignRoutine extends DrivePathRoutine {

    private final VisionConfig mVisionConfig = Configs.get(VisionConfig.class);
    private final Limelight mLimelight = Limelight.getInstance();
    private final int mVisionPipeline;
    private Pose2d mTargetPos;
    private double mTargetYawDegrees;
    private double mTrajectory;

    public DriveForwardAlignRoutine(int visionPipeline, double straightDistanceMeters) {
        mVisionPipeline = visionPipeline;
    }

    @Override
    public void update(Commands commands, @ReadOnly RobotState state) {
        //check if target, then if target is there create a trajectory once that is the current pos to target pos.
        commands.visionWanted = true;
        if (mLimelight.isTargetFound()) {
            mTargetYawDegrees = mLimelight.getYawToTarget();
            commands.setDriveVisionAlign(mVisionPipeline);
        }
    }


    @Override
    public boolean checkIfFinishedEarly(@ReadOnly RobotState state) {
        //TODO: need to implement
        return false;
    }

    @Override
    public Set<SubsystemBase> getRequiredSubsystems() {
        return Set.of(mDrive);
    }
}
