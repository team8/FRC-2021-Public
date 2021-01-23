package com.palyrobotics.frc2020.util.commands.commandclasses;

import java.io.IOException;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.ConfigBase;
import com.palyrobotics.frc2020.util.config.Configs;

public class Save extends AbstractCommand {

	private static final Save sInstance = new Save();

	public Save() {
		super("save");
	}

	@Override
	public String execute(ObjectMapper mapper) {
		if (mParams.length < 1) {
			return "Not enough parameters specified \nFormat is : save [config_name]";
		}

		String configName = mParams[0];
		Class<? extends ConfigBase> configClass = Configs.getClassFromName(configName);
		if (configClass == null) {
			return String.format("Config class does not exist: %s", configName);
		}

		try {
			Configs.saveOrThrow(configClass);
			return String.format("Saved config for %s", configName);
		} catch (IOException saveException) {
			var errorMessage = String.format(
					"File system error saving config %s - this should NOT happen!", configName);
			Log.error(Util.classToJsonName(getClass()), errorMessage, saveException);
			return errorMessage;
		}
	}

	public static Save getInstance() {
		return sInstance;
	}
}
