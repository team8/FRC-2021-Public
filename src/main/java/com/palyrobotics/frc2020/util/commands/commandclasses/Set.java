package com.palyrobotics.frc2020.util.commands.commandclasses;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.ConfigBase;
import com.palyrobotics.frc2020.util.config.Configs;

public class Set extends AbstractCommand {

	public Set() {
		super("set");
	}

	private static final Set sInstance = new Set();

	@Override
	public String execute(ObjectMapper mapper) {
		String result;
		if (mParams.length < 3) {
			return "Not enough parameters specified \n Format is : set [config_name] [field_name] [new_value]";
		}
		String configName = mParams[0];
		//get the config class from its name
		Class<? extends ConfigBase> configClass = Configs.getClassFromName(configName);
		if (configClass == null) {
			return String.format("Config class does not exist: %s", configName);
		}
		//get the config object to read
		ConfigBase configObject = Configs.get(configClass);
		//get the rest of the fields
		var allFieldNames = mParams[1];
		String[] fieldNames = allFieldNames == null ? null : allFieldNames.split("\\.");
		//originally all the json, can become a field if specified
		Object fieldValue = configObject, fieldParentValue = null;
		Field field = null;
		if (fieldNames != null && fieldNames.length != 0) {
			for (String fieldName : fieldNames) {
				try {
					//find the field (NOT FOR FULL FILE) that is trying to be set or get, get its type
					field = getField(field == null ? configClass : field.getType(), fieldName);
					fieldParentValue = fieldValue;
					fieldValue = field.get(fieldValue);
				} catch (Exception e) {
					return String.format("Unable to retrieve field: %s", fieldName);
				}

			}
		}

		String stringValue = mParams[2];

		//formatting and returning - try these commands in control center terminal for better understanding
		try {
			Object newFieldValue = mapper.readValue(stringValue, field.getType());
			try {
				Configs.set(configObject, fieldParentValue, field, newFieldValue);
			} catch (IllegalAccessException illegalAccessException) {
				var errorMessage = String.format("Error setting field %s", allFieldNames);
				Log.warn(Util.classToJsonName(getClass()), errorMessage, illegalAccessException);
				return errorMessage;
			}

			result = String.format("Set field %s on config %s to %s", allFieldNames,
					configName, stringValue);
		} catch (IOException parseException) {
			return String.format("Error parsing %s for field %s on config %s",
					stringValue, allFieldNames, configName);
		}

		return result;
	}

	private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		var fields = new HashMap<String, Field>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			fields.putAll(Arrays.stream(c.getDeclaredFields())
					.collect(Collectors.toMap(Field::getName, Function.identity())));
		}
		return Optional.ofNullable(fields.get(name)).orElseThrow(NoSuchFieldException::new);
	}

	public static Set getInstance() {
		return sInstance;
	}
}
