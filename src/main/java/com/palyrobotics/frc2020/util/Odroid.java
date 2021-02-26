package com.palyrobotics.frc2020.util;

import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.util.service.OdroidMessage;

public class Odroid {

	private static final String category = "Odroid";

	private Server server;

	private double radiansToBall = 0;

	public Odroid(int port) {
		server = new Server();
//		server.getKryo().register(Double.class);
		server.getKryo().register(OdroidMessage.class);
		server.getKryo().register(HashMap.class);
		try {
			server.addListener(new Listener() {

				@Override
				public void connected(Connection connection) {
					Log.info(category, "Connected!");
				}

				@Override
				public void disconnected(Connection connection) {
					Log.error(category, "Disconnected!");
				}

				@Override
				public void received(Connection connection, Object message) {
//					Log.info(category, "Received object: " + message.getClass());
					if (!message.getClass().getName().equals(HashMap.class.getName())) {
						Log.info(category, "unexpected class: " + message.getClass().getName());
						 return;
					}
					Object sent = ((HashMap<String, Object>) message).get("radians-to-ball");
					if (sent.getClass().equals(Double.class)) {
						radiansToBall = (double) sent;
					}
					Log.info(category, String.valueOf(radiansToBall));
				}
			});
			server.start();
			server.bind(port);

			Log.info(category, "Started server");
		} catch (IOException exception) {
			Log.error(category, "Failed to start server", exception);
		}
	}

	// might want to go to degrees
	public double getRadiansToBall() {
		return radiansToBall;
	}
}
