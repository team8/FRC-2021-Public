package com.palyrobotics.frc2020.config.constants;

import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

public class IndexerConstants {

	public static final InterpolatingDoubleTreeMap kTargetDistanceToIndexerVelocity = new InterpolatingDoubleTreeMap();

	static {
		kTargetDistanceToIndexerVelocity.put(0.0, 85.0);
//		kTargetDistanceToIndexerVelocity.put(100.0, 75.0);
//		kTargetDistanceToIndexerVelocity.put(215.0, 50.0);
	}

}
