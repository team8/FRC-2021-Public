package com.palyrobotics.frc2020.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class CircularBuffer<E> {

	private final int mWindowSize;
	private final LinkedList<E> mSamples = new LinkedList<>();

	public CircularBuffer(int windowSize) {
		mWindowSize = windowSize;
	}

	public void fillBuffer(List<E> list) {
		for (E value : list) {
			this.add(value);
		}
	}

	public void clear() {
		mSamples.clear();
	}

	public void add(E val) {
		mSamples.addLast(val);
		while (mSamples.size() > mWindowSize) {
			mSamples.removeFirst();
		}
	}

	public long numberOfOccurrences(E value) {
		return mSamples.stream()
				.filter(value::equals)
				.count();
	}

	public long numberOfOccurrences(Predicate<E> predicate) {
		return mSamples.stream()
				.filter(predicate)
				.count();
	}

	public LinkedList<E> getSamples() {
		return mSamples;
	}

	public int size() {
		return mSamples.size();
	}
}
