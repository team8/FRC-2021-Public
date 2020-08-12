package com.palyrobotics.frc2020.subsystems;

import java.util.*;

import com.palyrobotics.frc2020.config.subsystem.LightingConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.controllers.lighting.*;
import com.palyrobotics.frc2020.util.Color;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.LightingOutputs;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;

public class Lighting extends SubsystemBase {

	public enum State {
		OFF, IDLE, INIT, DISABLE, TARGET_FOUND, SHOOTER_FULLRPM, ROBOT_ALIGNED, CLIMB_DONE, INTAKE_EXTENDED, BALL_ENTERED, SPINNER_DONE, BALL_SHOT, DO_NOTHING
	}

	public abstract static class LEDController {

		protected static final double kZeroSpeed = 1e-4;

		protected LightingOutputs mOutputs = new LightingOutputs();
		protected Timer mTimer = new Timer();

		protected int mStartIndex;
		protected int mLastIndex;
		protected double mSpeed;
		protected int kPriority;

		protected LEDController(int startIndex, int lastIndex, int priority) {
			for (var i = 0; i <= Math.abs(lastIndex - startIndex); i++) {
				mOutputs.lightingOutput.add(new Color.HSV());
			}
			kPriority = priority;
			mTimer.reset();
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof LEDController) {
				LEDController otherController = (LEDController) object;
				return otherController.mStartIndex == this.mStartIndex && otherController.mLastIndex == this.mLastIndex && this.getClass().getName().equals(otherController.getClass().getName());
			}
			return false;
		}

		public final LightingOutputs update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
			updateSignal(commands, state);
			return mOutputs;
		}

		public abstract void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state);

		public boolean checkFinished() {
			return false;
		}
	}

	private static Lighting sInstance = new Lighting();
	private LightingConfig mConfig = Configs.get(LightingConfig.class);
	private AddressableLEDBuffer mOutputBuffer = new AddressableLEDBuffer(mConfig.ledCount);
	//private State mState;
	private PriorityQueue<State> mStates = new PriorityQueue<>(10, Comparator.comparingInt(this::getLightingEnumValueInt));
	private PriorityQueue<LEDController> mLEDControllers = new PriorityQueue<>(1, Comparator.comparingInt(o -> o.kPriority));

	private Lighting() {
	}

	public static Lighting getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		State wantedState = commands.lightingWantedState;
		if (RobotController.getBatteryVoltage() < mConfig.minVoltageToFunction) wantedState = State.OFF;
		boolean isNewState = mStates.contains(wantedState);
//		mState = wantedState;
		if (isNewState) {
			mStates.add(wantedState);
			int controllerPriority = getLightingEnumValueInt(wantedState);
			switch (wantedState) {
				case OFF:
					resetLedStrip();
					mLEDControllers.clear();
					mStates.clear();
					break;
				case IDLE:
					break;
				case INIT:
				case DISABLE:
					resetLedStrip();
					addToControllers(new OneColorController(mConfig.totalSegmentFirstIndex, mConfig.totalSegmentLastIndex, Color.HSV.kAqua, controllerPriority));
					break;
				case TARGET_FOUND:
					addToControllers(new FadeInFadeOutController(mConfig.spinnerSegmentFirstIndex,
							mConfig.spinnerSegmentLastIndex, Color.HSV.kYellow, 1, 2));
					break;
				case SPINNER_DONE:
					addToControllers(new OneColorController(mConfig.frontLeftSegmentFirstIndex, mConfig.frontRightSegmentLastIndex, Color.HSV.kBlue, 2, controllerPriority));
					break;
				case BALL_ENTERED:
					addToControllers(new DivergingBandsController(mConfig.frontLeftSegmentFirstIndex, mConfig.frontRightSegmentLastIndex, Color.HSV.kOrange, Color.HSV.kOff, 2, 1.0 / 6.0, 2, controllerPriority));
					addToControllers(new DivergingBandsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kOrange, Color.HSV.kOff, 3, 1.0 / 6.0, 2, controllerPriority));
					break;
				case CLIMB_DONE:
					addToControllers(new FadeInController(mConfig.totalSegmentFirstIndex,
							mConfig.totalSegmentLastIndex, Color.HSV.kPink, 0.5, 3, controllerPriority));
					break;
				case INTAKE_EXTENDED:
					addToControllers(new DivergingBandsController(mConfig.frontLeftSegmentFirstIndex, mConfig.frontRightSegmentLastIndex, Color.HSV.kPurple, Color.HSV.kOff, 2, 1.0 / 6.0, 2, controllerPriority));
					addToControllers(new DivergingBandsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kPurple, Color.HSV.kOff, 3, 1.0 / 6.0, 2, controllerPriority));
					break;
				case ROBOT_ALIGNED:
					addToControllers(new OneColorController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kLime, 2, controllerPriority));
					break;
				case SHOOTER_FULLRPM:
					addToControllers(new FadeInFadeOutController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kGreen, 0.5, 5, controllerPriority));
					break;
				case BALL_SHOT:
					addToControllers(new OneColorController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kBlue, 0.25, controllerPriority));
			}
		}

		resetLedStrip();
		for(LEDController ledController : mLEDControllers){
			if(ledController.checkFinished()){
				mStates.removeIf(state1 -> getLightingEnumValueInt(state1) == ledController.kPriority);
			}
		}
		mLEDControllers.removeIf(LEDController::checkFinished);
		/*if (mLEDControllers.removeIf(LEDController::checkFinished)) { //seems problematic
			mState = State.DO_NOTHING;
		}*/

		for (LEDController ledController : mLEDControllers) {
			LightingOutputs controllerOutput = ledController.update(commands, state);
			for (int i = 0; i < controllerOutput.lightingOutput.size(); i++) {
				Color.HSV hsvValue = controllerOutput.lightingOutput.get(i);
				mOutputBuffer.setHSV(i + ledController.mStartIndex, hsvValue.getH(), hsvValue.getS(), Math.min(hsvValue.getV(), mConfig.maximumBrightness));
			}
		}
	}

	private void addToControllers(LEDController controller) {
		mLEDControllers.add(controller);
	}

	private void resetLedStrip() {
		for (int i = 0; i < mOutputBuffer.getLength(); i++) {
			mOutputBuffer.setRGB(i, 0, 0, 0);
		}
	}

	public AddressableLEDBuffer getOutput() {
		return mOutputBuffer;
	}

	private int getLightingEnumValueInt(State state){
		State[] states = State.values();
		for(var i = 0;i < states.length;i++){
			if(state.equals(states[i])){
				return i;
			}
		}
		return -1;
	}
}
