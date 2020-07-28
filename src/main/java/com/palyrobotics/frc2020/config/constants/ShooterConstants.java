package com.palyrobotics.frc2020.config.constants;

import java.util.EnumMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

public class ShooterConstants {

	public static final Map<Shooter.HoodState, InterpolatingDoubleTreeMap> kTargetDistanceToVelocity = new EnumMap<>(Shooter.HoodState.class);
	public static final NavigableMap<Double, Shooter.HoodState> kTargetDistanceToHoodState = new TreeMap<>();
	public static final double kTimeToShootPerBallSeconds = 0.5;
	public static final double kMaxVelocity = 1.0;
	public static final double kMinVelocity = 0.0;
}
