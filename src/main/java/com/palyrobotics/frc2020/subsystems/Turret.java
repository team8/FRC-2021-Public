package com.palyrobotics.frc2020.subsystems;

import com.palyrobotics.frc2020.config.subsystem.TurretConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

public class Turret extends SubsystemBase {

	public enum TurretState {
		IDLE, VISION_ALIGN, CUSTOM_ANGLE_ALIGN
	}

	private static Turret sInstance = new Turret();
	private ControllerOutput mOutput = new ControllerOutput();
	private TurretConfig mConfig = Configs.get(TurretConfig.class);
	private Limelight mLimelight = Limelight.getInstance();

	private Turret() {
	}

	public static Turret getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		switch (commands.getTurretWantedState()) {
			case VISION_ALIGN:
				//todo: add better vision align for turret
				mOutput.setTargetPosition(mLimelight.getYawToTarget() + state.turretYawDegrees, mConfig.turretGains);
				break;
			case CUSTOM_ANGLE_ALIGN:
				mOutput.setTargetPosition(commands.getTurretWantedAngle(), mConfig.turretGains);
				break;
			case IDLE:
				mOutput.setIdle();
		}
	}

	public ControllerOutput getOutput() {
		return mOutput;
	}
}
