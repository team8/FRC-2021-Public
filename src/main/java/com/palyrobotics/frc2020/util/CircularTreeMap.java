package com.palyrobotics.frc2020.util;

import java.util.TreeMap;

public class CircularTreeMap<K, V> extends TreeMap<K, V> {
    private int mMaxSize;

    public CircularTreeMap(int maxSize) {
        mMaxSize = maxSize;
    }

    @Override
    public V put(K key, V value) {
        if (size() >= mMaxSize) {
            super.pollFirstEntry();
        }
        return super.put(key, value);
    }
}
