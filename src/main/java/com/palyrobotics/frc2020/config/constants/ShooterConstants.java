package com.palyrobotics.frc2020.config.constants;

import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

import java.util.EnumMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ShooterConstants {

    public static final Map<Shooter.HoodState, InterpolatingDoubleTreeMap> kTargetDistanceToVelocity = new EnumMap<>(Shooter.HoodState.class);
    public static final NavigableMap<Double, Shooter.HoodState> kTargetDistanceToHoodState = new TreeMap<>();
    public static final double kTimeToShootPerBallSeconds = 1.0;

}