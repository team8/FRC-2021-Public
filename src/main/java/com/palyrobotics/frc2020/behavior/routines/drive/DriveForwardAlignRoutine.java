package com.palyrobotics.frc2020.behavior.routines.drive;

import java.util.Set;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class DriveForwardAlignRoutine extends DrivePathRoutine {

	private final Limelight mLimelight = Limelight.getInstance();
	//Limelight controlled targeted point.
	private Pose2d mLimelightTarget;
	private Pose2d forwardTarget;

	private double mTargetYawRad;

	private boolean hasLocated;

	public DriveForwardAlignRoutine(Pose2d forwardTarget) {
		super(forwardTarget);
		this.forwardTarget = forwardTarget;
	}

	@Override
	public void update(Commands commands, @ReadOnly RobotState state) {
		//check if target, then if target is there create a trajectory once that is the current pos to target pos.
		commands.visionWanted = true;
		if (mLimelight.isTargetFound()) {
			hasLocated = true;
			mTargetYawRad = Math.toRadians(mLimelight.getYawToTarget());
			mLimelightTarget = findPointOrthagonalCurrentPosFarPos(mTargetYawRad, state.drivePoseMeters, forwardTarget);
			System.out.println("targetPosX:" + mLimelightTarget.getTranslation().getX() + " targetPosY" + mLimelightTarget.getTranslation().getY());
//			commands.addWantedRoutine(new DrivePathRoutine(mLimelightTarget));
		} else {
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

	public static Pose2d findPointOrthagonalCurrentPosFarPos(double theta, Pose2d robotPos, Pose2d targetPos) {
		Translation2d robotTransl = robotPos.getTranslation();
		Translation2d targetTransl = targetPos.getTranslation();

		double orthogonalDist = robotTransl.getDistance(targetTransl);
		double run = targetTransl.getX() - robotTransl.getX();
		double rise = targetTransl.getY() - robotTransl.getY();

		double magnitudeVector = Math.hypot(run, rise);
		// because perpendicular
		double xDisplacement = rise / magnitudeVector * orthogonalDist * Math.signum(theta);
		double yDisplacement = -run / magnitudeVector * orthogonalDist * Math.signum(theta);

		Translation2d orthogonalPoint = targetTransl.plus(new Translation2d(xDisplacement, yDisplacement));

		return new Pose2d(orthogonalPoint, new Rotation2d(robotPos.getRotation().getRadians() + theta));

	}
}
