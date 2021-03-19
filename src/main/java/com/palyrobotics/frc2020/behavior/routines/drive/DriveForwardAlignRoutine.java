package com.palyrobotics.frc2020.behavior.routines.drive;

import static com.palyrobotics.frc2020.util.Util.getDifferenceInAngleDegrees;

import java.util.Objects;
import java.util.Set;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class DriveForwardAlignRoutine extends DrivePathRoutine {

    private final VisionConfig mVisionConfig = Configs.get(VisionConfig.class);
    private final Limelight mLimelight = Limelight.getInstance();
    private final int mVisionPipeline;
    //Limelight controlled targeted point.
    private Pose2d mLimelightTarget;

    private double mTargetYawDegrees;
    private double mTrajectory;

    private boolean hasLocated;

    public DriveForwardAlignRoutine(Pose2d forwardTarget, int visionPipeline) {
        super(forwardTarget);
        mVisionPipeline = visionPipeline;
    }

    @Override
    public void update(Commands commands, @ReadOnly RobotState state) {
        //check if target, then if target is there create a trajectory once that is the current pos to target pos.
        commands.visionWanted = true;
        if (mLimelight.isTargetFound()) {
                hasLocated = true;
            mTargetYawDegrees = mLimelight.getYawToTarget();
           //TODO: fill this out mLimelightTarget = new Pose2d();
            commands.addWantedRoutine(new DrivePathRoutine());
        }
        else{
            super.update(commands, state);
        }
    }

    @Override
    public boolean checkIfFinishedEarly(@ReadOnly RobotState state) {
        return hasLocated;
    }
    @Override
    public Set<SubsystemBase> getRequiredSubsystems() {
        return Set.of(mDrive);
    }

    public static Pose2d findPointOrthagonalCurrentPosFarPos(double theta, Pose2d robotPos, Pose2d targetPos){
        Translation2d robotTransl = robotPos.getTranslation();
        Translation2d targetTransl = targetPos.getTranslation();

        double orthogonalDist = robotTransl.getDistance(targetTransl);
        double run = targetTransl.getX() - robotTransl.getX();
        double rise = targetTransl.getY() - robotTransl.getY();

        //create inverse unit vector that is orthogonal to original line



        orthogonalDist *

    }
}
