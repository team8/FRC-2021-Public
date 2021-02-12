package com.palyrobotics.frc2020.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class Odroid {

    private static final String catagory = "Odroid";

    private Server server;
    private String address;
    private int port;

    public Odroid(String address, int port) {
        this.address = address;
        this.port = port;

        server = new Server();
        server.getKryo().setRegistrationRequired(false);

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
                    // TODO: work on what this should do
                }
            });
            server.start();
            server.bind(port);
            Log.info(catagory, "Started server");
        } catch (IOException exception) {
            Log.error(catagory, "Failed to start server", exception);
        }
    }
}
