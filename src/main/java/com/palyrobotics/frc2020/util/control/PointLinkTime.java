package com.palyrobotics.frc2020.util.control;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class PointLinkTime {

	private String mRoutineName;
	private final Pose2d mPose;
	private final double mTime;

	public PointLinkTime(Translation2d translation, Rotation2d rotation, double time, String routineName) {
		mPose = new Pose2d(translation, rotation);
		this.mTime = time;
		this.mRoutineName = routineName;
	}

	public PointLinkTime(Pose2d pose, double time, String routineName) {
		mPose = pose;
		this.mTime = time;
		this.mRoutineName = routineName;
	}

	public double getTime() {
		return mTime;
	}

	public Pose2d getPose() {
		return mPose;
	}

	public String getRoutineName() {
		return mRoutineName;
	}

	@Override
	public String toString() {
		return "PointLinkTime{" +
				"mPose=" + mPose +
				", mTime=" + mTime +
				'}';
	}
}
