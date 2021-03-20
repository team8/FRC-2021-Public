package com.palyrobotics.frc2020.util;

import java.awt.*;

public class Circle {

	float radius;
	Point center;

	public Circle(float radius, Point center) {
		this.radius = radius;
		this.center = center;
	}

	@Override
	public String toString() {
		return "Circle{" +
				"radius=" + radius +
				", center=" + center +
				'}';
	}
}