package com.palyrobotics.frc2020.util.commands;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.sourceforge.argparse4j.inf.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palyrobotics.frc2020.robot.*;
import com.palyrobotics.frc2020.util.commands.commandclasses.AbstractCommand;
import com.palyrobotics.frc2020.util.commands.commandclasses.Set;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.service.RobotService;

public class CommandReceiverService implements RobotService {

	private static final int kPort = 5808;

	private static ObjectMapper sMapper = Configs.getMapper();

	private HashMap<String, AbstractCommand> mCommandMap;
	private Server mServer;
	private AtomicString mResult = new AtomicString(), mCommand = new AtomicString();

	public CommandReceiverService() {
		mCommandMap = new HashMap<>();
		mCommandMap.put("set", Set.getInstance());
	}

	@Override
	public void start() {
		mServer = new Server();
		mServer.getKryo().setRegistrationRequired(false);
		try {
			mServer.addListener(new Listener() {

				@Override
				public void connected(Connection connection) {
					Log.info(getConfigName(), "Connected!");
				}

				@Override
				public void disconnected(Connection connection) {
					Log.error(getConfigName(), "Disconnected!");
				}

				@Override
				public void received(Connection connection, Object message) {
					if (message instanceof String) {
						var command = (String) message;
						mCommand.set(command);
						try {
							String result = mResult.waitAndGet();
							mServer.sendToTCP(connection.getID(), result);
						} catch (InterruptedException interruptedException) {
							mServer.close();
							Thread.currentThread().interrupt();
						}
					}
				}
			});
			mServer.start();
			mServer.bind(kPort);
			Log.info(getConfigName(), "Started server");
		} catch (IOException exception) {
			Log.error(getConfigName(), "Failed to start server", exception);
		}
	}

	@Override
	public void update(@ReadOnly RobotState state, Commands commands) {
		mCommand.tryGetAndReset(command -> {
			if (command == null) return;
			String result = executeCommand(command, commands);
			mResult.setAndNotify(result);
		});
	}

	public String executeCommand(String command, Commands commands) {
		if (command == null) throw new IllegalArgumentException("Command can not be null!");
		String result = "";
		String[] commandSplit = command.split(" ");
		String commandName = commandSplit[0];
		String[] commandParams = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
		if (mCommandMap.containsKey(commandName)) {
			try {
				AbstractCommand commandToExecute = mCommandMap.get(commandName);
				commandToExecute.setParams(commandParams);
				result = commandToExecute.execute(sMapper);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				result = exceptionAsString;
				return result;
			}

		} else {
			result = String.format("Command does not exist: %s", commandName);
		}

		return result;
	}

	/**
	 * Allows us to get fields that belong to super-classes as well
	 */
	private Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		var fields = new HashMap<String, Field>();
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			fields.putAll(Arrays.stream(c.getDeclaredFields())
					.collect(Collectors.toMap(Field::getName, Function.identity())));
		}
		return Optional.ofNullable(fields.get(name)).orElseThrow(NoSuchFieldException::new);
	}

	public void stop() {
		mServer.stop();
	}
}
