package com.palyrobotics.frc2020.util.control;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

public class PointLinkTime {

	Pose2d mPose;
	double mTime;

	public PointLinkTime(Translation2d translation, Rotation2d rotation, double time) {
		mPose = new Pose2d(translation, rotation);
		this.mTime = time;
	}

	public PointLinkTime(Pose2d pose, double time) {
		mPose = pose;
		this.mTime = time;
	}

	public double getTime() {
		return mTime;
	}

	public Pose2d getPose() {
		return mPose;
	}

	@Override
	public String toString() {
		return "PointLinkTime{" +
				"mPose=" + mPose +
				", mTime=" + mTime +
				'}';
	}
}
