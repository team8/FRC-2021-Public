package com.palyrobotics.frc2021.util.config;

public abstract class ConfigBase {

	@Override
	public String toString() {
		return Configs.toJson(this);
	}

	void onPostUpdate() {
	}
}
