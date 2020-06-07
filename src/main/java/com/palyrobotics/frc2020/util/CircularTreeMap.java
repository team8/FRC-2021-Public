package com.palyrobotics.frc2020.util;

import java.util.TreeMap;

public class CircularTreeMap<V> extends TreeMap<Double, V> {

	private int mMaxSize;

	public CircularTreeMap(int maxSize) {
		mMaxSize = maxSize;
	}

	@Override
	public V put(Double key, V value) {
		if (size() >= mMaxSize) {
			super.pollFirstEntry();
		}
		return super.put(key, value);
	}

	@Override
	public V get(Object key) {
		Double floorKey = floorKey((Double) key),
				ceilingKey = ceilingKey((Double) key);
		return  (floorKey == null) ? super.get(ceilingKey) :
				(ceilingKey == null) ? super.get(floorKey) :
				(ceilingKey - (Double) key > (Double) key - floorKey) ? super.get(floorKey) : super.get(ceilingKey);
	}
}
