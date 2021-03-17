package com.palyrobotics.frc2020.config.constants;

import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

public class IndexerConstants {

	public static final InterpolatingDoubleTreeMap kTargetDistanceToIndexerVelocity = new InterpolatingDoubleTreeMap();

	static {
//		Speed
		kTargetDistanceToIndexerVelocity.put(0.0, 87.0);
//      Accuracy
//		kTargetDistanceToIndexerVelocity.put(0.0, 87.0);
//		kTargetDistanceToIndexerVelocity.put(100.0, 83.0);
//		kTargetDistanceToIndexerVelocity.put(215.0, 60.0);
//		kTargetDistanceToIndexerVelocity.put(300.0, 40.0);
	}

}
