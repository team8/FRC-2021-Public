package com.palyrobotics.frc2020.util;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class Odroid {

	private static final String category = "Odroid";

	private final Client client;

	private final ArrayList<Circle> balls = new ArrayList<>();

	public Odroid(int port) {
		client = new Client();
		client.getKryo().register(HashMap.class);
		client.getKryo().register(ArrayList.class);
		client.getKryo().register(java.awt.Point.class);
		try {
			client.addListener(new Listener() {

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
					var radii = new ArrayList<Float>();
					var centers = new ArrayList<Point>();
					sent = ((HashMap<String, Object>) message).get("radii");
					if (sent.getClass().equals(ArrayList.class)) {
						radii = (ArrayList<Float>) sent;
					}
					sent = ((HashMap<String, Object>) message).get("centers");
					if (sent.getClass().equals(ArrayList.class)) {
						centers = (ArrayList<Point>) sent;
					}

					balls.clear();
					for (int i = 0; i < radii.size() && i < centers.size(); i++) {
						balls.add(new Circle(radii.get(i), centers.get(i)));
					}
				}
			});
			client.start();
			client.connect(100, "127.0.0.1", port, port); // TODO: no more magic numbers, also which one is the ip? prob look at old code to find out or smth

			Log.info(category, "Started server");
		} catch (IOException exception) {
			Log.error(category, "Failed to start client", exception);
		}
	}

	public ArrayList<Circle> getBalls() {
		return balls;
	}
}
