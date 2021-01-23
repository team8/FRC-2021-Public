package com.palyrobotics.frc2020.util.commands.commandclasses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palyrobotics.frc2020.util.config.ConfigBase;
import com.palyrobotics.frc2020.util.config.Configs;

public class Reload extends AbstractCommand {

	private static final Reload sInstance = new Reload();

	public Reload() {
		super("reload");
	}

	@Override
	public String execute(ObjectMapper mapper) {
		if (mParams.length < 1) {
			return "Not enough parameters specified \nFormat is : reload [config_name]";
		}
		String configName = mParams[0];
		//get the config class from its name
		Class<? extends ConfigBase> configClass = Configs.getClassFromName(configName);
		if (configClass == null) {
			return String.format("Config class does not exist: %s", configName);
		}
		boolean didReload = Configs.reload(configClass);
		return String.format(didReload ? "Reloaded config %s" : "Did not reload config %s",
				configName);
	}

	public static Reload getInstance() {
		return sInstance;
	}
}
