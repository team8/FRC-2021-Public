package com.palyrobotics.frc2020.config.constants;

import com.palyrobotics.frc2020.util.InterpolatingDoubleTreeMap;

public class IndexerConstants {

    public static final InterpolatingDoubleTreeMap kTargetDistanceToIndexerVelocity = new InterpolatingDoubleTreeMap();

    static {
        kTargetDistanceToIndexerVelocity.put(0.0, 0.0); // Need robot to add value, this is how to do them
    }

}
