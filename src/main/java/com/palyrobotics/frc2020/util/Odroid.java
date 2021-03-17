package com.palyrobotics.frc2020.util;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Odroid {

	private static final String category = "Odroid";

	private Server server;

	private ArrayList<Float> radii = new ArrayList<>();
	private ArrayList<Point> centers = new ArrayList<>();

	public Odroid(int port) {
		server = new Server();
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
					if (!message.getClass().getName().equals(HashMap.class.getName())) {
						return;
					}
					Object sent;
					sent = ((HashMap<String, Object>) message).get("radii");
					if (sent.getClass().equals(ArrayList.class)) {
						radii = (ArrayList<Float>) sent;
					}
					sent = ((HashMap<String, Object>) message).get("centers");
					if (sent.getClass().equals(ArrayList.class)) {
						centers = (ArrayList<Point>) sent;
					}
					Log.info(category, String.valueOf(centers.get(0).x)); // TODO: might give error but whatever ill delete this later cus its just for debug
				}
			});
			server.start();
			server.bind(port);

			Log.info(category, "Started server");
		} catch (IOException exception) {
			Log.error(category, "Failed to start server", exception);
		}
	}

	public ArrayList<Float> getRadii() {
		return radii;
	}

	public ArrayList<Point> getCenters() {
		return centers;
	}
}
