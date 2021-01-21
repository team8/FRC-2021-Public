package com.palyrobotics.frc2020.util.commands.commandclasses;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractCommand {

	protected String[] mParams;
	public String name;

	public AbstractCommand(String name) {
		this.name = name;
	}

	public abstract String execute(ObjectMapper mapper);

	public String[] setParams(String[] args) {
		mParams = args;
		return mParams;
	}
}
