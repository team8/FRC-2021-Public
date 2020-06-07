package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.constants.FieldConstants;
import com.palyrobotics.frc2020.config.constants.TurretConstants;
import com.palyrobotics.frc2020.config.subsystem.TurretConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.util.Units;

public class Turret extends SubsystemBase {

    public enum TurretState {
        IDLE, TARGET_ALIGN, CUSTOM_ANGLE_SETPOINT
    }

    private static Turret sInstance = new Turret();
    private ControllerOutput mOutput = new ControllerOutput();
    private TurretConfig mConfig = Configs.get(TurretConfig.class);
    private Limelight mLimelight = Limelight.getInstance();
    private MedianFilter mVisionPnPXFilter = new MedianFilter(mConfig.visionPnPMedianFilterSize);
    private MedianFilter mVisionPnPYFilter = new MedianFilter(mConfig.visionPnPMedianFilterSize);

    private Turret() {
    }

    public static Turret getInstance() {
        return sInstance;
    }

    @Override
    public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
        switch (commands.getTurretWantedState()) {
            case TARGET_ALIGN:
				/*
				Need to implement the following:
				Latency compensation, accurate feedback control, feedback control with nonzero velocity reference
				Accurate feedforward that accounts for drivetrain motion
				*/


				Pose2d robotOdometryPose = state.pastPoses.lastEntry().getValue();
				Translation2d visionRobotTranslation = new Translation2d(FieldConstants.fieldDimensions.getX() - Units.inchesToMeters(mVisionPnPYFilter.calculate(mLimelight.getPnPTranslationY())),
                        Units.inchesToMeters(mVisionPnPXFilter.calculate(mLimelight.getPnPTranslationX())) + FieldConstants.targetFieldLocation.getY());
                Pose2d visionPose = new Pose2d(visionRobotTranslation, robotOdometryPose.getRotation()); //todo: try to derive rotation through vision.

                double latencyCompensationTimestamp = Timer.getFPGATimestamp() - mLimelight.imageCaptureLatency / 100.0 - mLimelight.getPipelineLatency() / 100.0; //pose look back time to account for vision latency
                Pose2d oldLatencyCompensatedPose = state.pastPoses.get(latencyCompensationTimestamp); //finds latency compensated pose
                Pose2d adjustedPose = robotOdometryPose.plus(visionPose.minus(oldLatencyCompensatedPose)); // corrects odometry using the vision derived pose and the old latency compensated pose : current_pose += (vision_pose - old_pose)
                Pose2d oldPose = state.pastPoses.get(Timer.getFPGATimestamp() - mConfig.poseChangeLookBackSec);
                Pose2d nextDrivePredictedPose = predictNextPose(adjustedPose, oldPose);

                Transform2d drivetrain2TurretTransform = new Transform2d(new Translation2d(TurretConstants.drivetrain2TurretX,
                        TurretConstants.drivetrain2TurretY), Rotation2d.fromDegrees(state.turretYawDegrees - 90)); //drivetrain2TurretTransform stores the turret pose in relation to the drivetrain
                Pose2d nextTurretPredictedPose = nextDrivePredictedPose.transformBy(drivetrain2TurretTransform); //accounts for pose offset between drivetrain and turret

                Translation2d nextTurretPredictedTranslation = nextTurretPredictedPose.getTranslation();
                double turretRelativeBearing = Math.toDegrees(Math.atan2(nextTurretPredictedTranslation.getY() - FieldConstants.targetFieldLocation.getY(),
                        FieldConstants.targetFieldLocation.getX() - nextTurretPredictedTranslation.getY())); //finds angle between predicted pose of turret and the target
                double turretDegAngleError = turretRelativeBearing + state.turretYawDegrees;
                mOutput.setTargetPosition(turretDegAngleError, mConfig.turretGains);
                break;
            case CUSTOM_ANGLE_SETPOINT:
                mOutput.setTargetPosition(commands.getTurretWantedAngle(), mConfig.turretGains);
                break;
            case IDLE:
                mOutput.setTargetPosition(TurretConstants.turretAngleHardStopRange / 2, mConfig.turretGains);
        }
    }

    private Pose2d predictNextPose(Pose2d currentPose, Pose2d oldPose) {
        Transform2d poseChange = currentPose.minus(oldPose); //finds change in pose between current pose and a pose mConfig.poseChangeLookBackSec ms before.
        Twist2d poseChangeTwist2D = new Twist2d(poseChange.getTranslation().getX(),
                poseChange.getTranslation().getY(), poseChange.getRotation().getRadians()); //converts transform2d to twist2d
        return currentPose.exp(poseChangeTwist2D); // predicts next pose using previous change in pose
    }

    public ControllerOutput getOutput() {
        return mOutput;
    }
}
