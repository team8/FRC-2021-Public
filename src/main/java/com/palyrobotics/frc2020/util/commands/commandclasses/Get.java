package com.palyrobotics.frc2020.util.commands.commandclasses;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palyrobotics.frc2020.util.config.ConfigBase;
import com.palyrobotics.frc2020.util.config.Configs;

public class Get extends AbstractCommand {

	private static final Get sInstance = new Get();

	public Get() {
		super("get");
	}

	@Override
	public String execute(ObjectMapper mapper) {
		if (mParams.length < 1) {
			return "Not enough parameters specified \nFormat is : get [config_name] (optional)[field_name] (optional)[is_raw]";
		}
		String configName = mParams[0];
		switch (configName) {
			case "configs":
				//returns all config names
				return String.join(",", Configs.getActiveConfigNames());
		}
		Class<? extends ConfigBase> configClass = Configs.getClassFromName(configName);
		if (configClass == null) {
			return String.format("Config class does not exist: %s", configName);
		}
		//get the config object to read
		ConfigBase configObject = Configs.get(configClass);
		//get the rest of the fields
		var allFieldNames = "";
		boolean isRaw = false;
		if (mParams.length > 1) {
			allFieldNames = mParams[1];
		}
		String[] fieldNames = allFieldNames == "" ? null : allFieldNames.split("\\.");
		if (allFieldNames.equals("--raw")) {
			fieldNames = null;
			isRaw = true;
		}
		//originally all the json, can become a field if specified
		Object fieldValue = configObject;
		Field field = null;
		if (fieldNames != null && fieldNames.length != 0) {
			for (String fieldName : fieldNames) {
				try {
					//find the field (NOT FOR FULL FILE) that is trying to be set or get, get its type
					field = getField(field == null ? configClass : field.getType(), fieldName);
					fieldValue = field.get(fieldValue);
				} catch (Exception e) {
					return String.format("Unable to retrieve field: %s", fieldName);
				}

			}
		}
		if (mParams.length > 2 && !isRaw) {
			if (mParams[2].equals("--raw")) {
				isRaw = true;
			}
		}
		//return the field you want - be it the whole config or one field
		String display = Configs.toJson(fieldValue);
		//formatting and returning - try these commands in control center terminal for better understanding
		return isRaw ? display :
				String.format("[%s] %s: %s", configName,
						allFieldNames == null ? "all" : allFieldNames, display);
	}

	private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		var fields = new HashMap<String, Field>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			fields.putAll(Arrays.stream(c.getDeclaredFields())
					.collect(Collectors.toMap(Field::getName, Function.identity())));
		}
		return Optional.ofNullable(fields.get(name)).orElseThrow(NoSuchFieldException::new);
	}

	public static Get getInstance() {
		return sInstance;
	}
}
