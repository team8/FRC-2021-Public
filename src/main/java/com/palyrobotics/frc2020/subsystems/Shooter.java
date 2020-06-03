package com.palyrobotics.frc2020.subsystems;

import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToHoodState;
import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToVelocity;
import static com.palyrobotics.frc2020.util.Util.clamp;

import java.util.Map;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.MedianFilter;

public class Shooter extends SubsystemBase {

	public enum ShooterState {
		IDLE, CUSTOM_VELOCITY, VISION_VELOCITY
	}

	public enum HoodState {
		LOW, MIDDLE, HIGH
	}

	private static Shooter sInstance = new Shooter();
	private ShooterConfig mConfig = Configs.get(ShooterConfig.class);
	private Limelight mLimelight = Limelight.getInstance();
	private ControllerOutput mFlywheelOutput = new ControllerOutput();
	private boolean mBlockingOutput, mHoodOutput;
	private MedianFilter mVisionDistanceFilter = new MedianFilter(15);
	private MedianFilter mVelocityFilter = new MedianFilter(15);
	private HoodState mPreviousHoodState = HoodState.LOW;

	private Shooter() {
	}

	public static Shooter getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		mFlywheelOutput.setTargetVelocity(getTargetFlywheelVelocity(commands, getVisionHoodState()), mConfig.flywheelGains);

		HoodState wantedHoodState = commands.getShooterWantedState() == ShooterState.VISION_VELOCITY ?
				getVisionHoodState() :
				commands.getShooterWantedState() == ShooterState.CUSTOM_VELOCITY ?
						commands.getHoodWantedState() :
						HoodState.LOW;
		applyHoodState(state, wantedHoodState);

		mPreviousHoodState = wantedHoodState;
	}

	private double getTargetFlywheelVelocity(@ReadOnly Commands commands, HoodState hoodState) {
		double targetFlywheelVelocity = 0;
		switch (commands.getShooterWantedState()) {
			case VISION_VELOCITY:
				Double targetDistance = getTargetDistance();
				if (targetDistance != null) {
					targetFlywheelVelocity = mVelocityFilter.calculate(kTargetDistanceToVelocity.get(hoodState).getInterpolated(targetDistance));
					break;
				}
			case CUSTOM_VELOCITY:
				targetFlywheelVelocity = commands.getShooterWantedCustomFlywheelVelocity();
				break;
		}
		return clamp(targetFlywheelVelocity, 0, mConfig.maxVelocity);
	}

	private Double getTargetDistance() {
		if (mLimelight.isTargetFound()) {
			return mVisionDistanceFilter.calculate(mLimelight.getEstimatedDistanceInches());
		}
		return null;
	}

	private HoodState getVisionHoodState() {
		Double targetDistanceInches = getTargetDistance();
		if (targetDistanceInches == null) {
			return HoodState.LOW;
		}
		Map.Entry<Double, HoodState> floorEntry = kTargetDistanceToHoodState.floorEntry(targetDistanceInches),
				ceilingEntry = kTargetDistanceToHoodState.ceilingEntry(targetDistanceInches),
				closestEntry = ceilingEntry == null ? floorEntry : (targetDistanceInches - floorEntry.getKey()) < (ceilingEntry.getKey() - targetDistanceInches) ? floorEntry : ceilingEntry;

		return Math.abs(closestEntry.getKey() - targetDistanceInches) > mConfig.hoodSwitchDistanceThreshold ? closestEntry.getValue() : mPreviousHoodState;
	}

	private void applyHoodState(@ReadOnly RobotState state, HoodState hoodState) {
		if (!state.shooterHoodTransitioning) {
			switch (hoodState) {
				case HIGH:
					mHoodOutput = true;
				case MIDDLE:
					if (mBlockingOutput) {
						mHoodOutput = false;
					}
					mHoodOutput = true;
					mBlockingOutput = true;
				case LOW:
					mBlockingOutput = false;
					mHoodOutput = false;
			}
		}
	}

	public ControllerOutput getFlywheelOutput() {
		return mFlywheelOutput;
	}

	public boolean getBlockingOutput() {
		return mBlockingOutput;
	}

	public boolean getHoodOutput() {
		return mHoodOutput;
	}

}
