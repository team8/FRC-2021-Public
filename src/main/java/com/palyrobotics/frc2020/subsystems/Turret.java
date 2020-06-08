package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.constants.FieldConstants;
import com.palyrobotics.frc2020.config.constants.TurretConstants;
import com.palyrobotics.frc2020.config.subsystem.TurretConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.util.control.SynchronousPIDF;
import com.palyrobotics.frc2020.vision.Limelight;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.util.Units;

import static com.palyrobotics.frc2020.config.constants.TurretConstants.turretAngleMultiplier;
import static com.palyrobotics.frc2020.util.Util.calculateHeadingDeg;
import static com.palyrobotics.frc2020.util.Util.clamp;

public class Turret extends SubsystemBase {

    public enum TurretState {
        IDLE, TARGET_ALIGN, CUSTOM_ANGLE_SETPOINT
    }

    private static Turret sInstance = new Turret();
    private ControllerOutput mOutput = new ControllerOutput();
    private TurretConfig mConfig = Configs.get(TurretConfig.class);
    private Limelight mLimelight = Limelight.getInstance();
    private final SynchronousPIDF mPidController = new SynchronousPIDF();
    private MedianFilter mVisionPnPXFilter = new MedianFilter(mConfig.visionPnPMedianFilterSize),
            mVisionPnPYFilter = new MedianFilter(mConfig.visionPnPMedianFilterSize);

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
				Latency compensation, accurate feedback control, feedback control with nonzero velocity reference
				Accurate feedforward that accounts for drivetrain motion
				*/

                //todo: Put turret/drivetrain prediction pose in robot state for shooter subsystem access?
                Pose2d robotOdometryPose = state.pastPoses.lastEntry().getValue();
                Pose2d visionPose = calculateVisionPose(robotOdometryPose);
                Pose2d adjustedPose = applyLatencyCompensation(state, robotOdometryPose, visionPose);
                Pose2d oldPose = state.pastPoses.get(Timer.getFPGATimestamp() - mConfig.poseChangeLookBackSec);
                Pose2d nextDrivePredictedPose = predictNextPose(adjustedPose, oldPose);
                Pose2d nextTurretPredictedPose = drive2TurretPose(state, nextDrivePredictedPose);
                Translation2d nextTurretPredictedTranslation = nextTurretPredictedPose.getTranslation();
                double turretRelativeBearing = calculateHeadingDeg(
                        nextTurretPredictedTranslation.getY() - FieldConstants.targetFieldLocation.getY(),
                        FieldConstants.targetFieldLocation.getX() - nextTurretPredictedTranslation.getX());
                mPidController.setPIDF(mConfig.turretGains.p, mConfig.turretGains.i, mConfig.turretGains.d, mConfig.turretGains.f);

                double turretAngleError = nextTurretPredictedPose.getRotation().getDegrees() - turretRelativeBearing;
                double turretBoundPOMultiplier = (turretAngleError < 0 && state.turretYawDegrees > TurretConstants.turretAngleSoftStopRange)
                        || (turretAngleError > 0 && state.turretYawDegrees < (TurretConstants.turretAngleHardStopRange - TurretConstants.turretAngleSoftStopRange))
                        ? turretAngleMultiplier.getInterpolated(state.turretYawDegrees) : 1; // Multiplier to prevent turret from hitting side bounds

                double turretFF = clamp((nextDrivePredictedPose.getTranslation().getY() - adjustedPose.getTranslation().getY()) * mConfig.motionFFMultiplier, -mConfig.maxTurretFF, mConfig.maxTurretFF); //feedforward that accounts for drivetrain motion
				mOutput.setPercentOutput(clamp(mPidController.calculate(turretAngleError * turretBoundPOMultiplier) + turretFF, -mConfig.maxTurretPO, mConfig.maxTurretPO));
                break;
            case CUSTOM_ANGLE_SETPOINT:
                mOutput.setTargetPosition(clamp(commands.getTurretWantedAngle(), 0, 180), mConfig.turretGains);
                break;
            case IDLE:
                mOutput.setTargetPosition(TurretConstants.turretAngleHardStopRange / 2, mConfig.turretGains);
        }
    }

    private Pose2d calculateVisionPose(Pose2d robotOdometryPose) {
        double metersPnPTranslationX = Units.inchesToMeters(mVisionPnPXFilter.calculate(mLimelight.getPnPTranslationX())),
                metersPnPTranslationY = Units.inchesToMeters(mVisionPnPYFilter.calculate(mLimelight.getPnPTranslationY()));
        if (metersPnPTranslationX != 0 || metersPnPTranslationY != 0) {
            Translation2d visionRobotTranslation = new Translation2d(
                    FieldConstants.fieldDimensions.getX() - metersPnPTranslationY,
                    metersPnPTranslationX + FieldConstants.targetFieldLocation.getY()); //todo: maybe account for limelight and drivetrain pose offset
            return new Pose2d(visionRobotTranslation, robotOdometryPose.getRotation()); //todo: try to derive rotation through vision.
        }
        return null;
    }

    private Pose2d applyLatencyCompensation(RobotState state, Pose2d robotOdometryPose, Pose2d visionPose) {
        double latencyCompensationTimestamp = Timer.getFPGATimestamp() - mLimelight.imageCaptureLatency / 100.0 - mLimelight.getPipelineLatency() / 100.0; //pose look back time to account for vision latency
        Pose2d oldLatencyCompensatedPose = state.pastPoses.get(latencyCompensationTimestamp); //finds latency compensated pose
        return visionPose != null ? robotOdometryPose.plus(visionPose.minus(oldLatencyCompensatedPose)) : robotOdometryPose.plus(robotOdometryPose.minus(oldLatencyCompensatedPose)); // corrects odometry using the vision derived pose and the old latency compensated pose : current_pose += (vision_pose - old_pose)
    }

    private Pose2d predictNextPose(Pose2d currentPose, Pose2d oldPose) {
        Transform2d poseChange = currentPose.minus(oldPose); //finds change in pose between current pose and a pose mConfig.poseChangeLookBackSec ms before.
        Twist2d poseChangeTwist2d = new Twist2d(poseChange.getTranslation().getX(),
                poseChange.getTranslation().getY(), poseChange.getRotation().getRadians()); //converts transform2d to twist2d
        return currentPose.exp(poseChangeTwist2d); // predicts next pose using previous change in pose
    }

    private Pose2d drive2TurretPose(RobotState state, Pose2d drivePose) {
        Transform2d drivetrain2TurretTransform = new Transform2d(new Translation2d(TurretConstants.drivetrain2TurretX,
                TurretConstants.drivetrain2TurretY), Rotation2d.fromDegrees(state.turretYawDegrees - 90)); //drivetrain2TurretTransform stores the turret pose in relation to the drivetrain
        return drivePose.transformBy(drivetrain2TurretTransform); //accounts for pose offset between drivetrain and turret

    }

    public ControllerOutput getOutput() {
        return mOutput;
    }
}
