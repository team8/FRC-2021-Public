package com.palyrobotics.frc2020.util;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.util.service.OdroidMessage;

public class Odroid {

	private static final String catagory = "Odroid";

	private Server server;

	private double radiansToBall = 0;

	public Odroid(int port) {
		server = new Server();
		server.getKryo().register(OdroidMessage.class);

		try {
			server.addListener(new Listener() {

				@Override
				public void connected(Connection connection) {
					Log.info(catagory, "Connected!");
				}

				@Override
				public void disconnected(Connection connection) {
					Log.error(catagory, "Disconnected!");
				}

				@Override
				public void received(Connection connection, Object message) {
					if (!message.getClass().equals(OdroidMessage.class)) {
						return;
					}

					// it would be easier just to send a double, but i feel like this is better in case
					// that more stuff needs to be transmitted
					radiansToBall = ((OdroidMessage) message).radiansToBall;
					Log.info(catagory, "" + radiansToBall);
				}
			});
			server.start();
			server.bind(port);

			Log.info(catagory, "Started server");
		} catch (IOException exception) {
			Log.error(catagory, "Failed to start server", exception);
		}
	}

	// might want to go to degrees
	public double getRadiansToBall() {
		return radiansToBall;
	}
}
