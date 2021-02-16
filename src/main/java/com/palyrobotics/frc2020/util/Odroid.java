package com.palyrobotics.frc2020.util;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.palyrobotics.frc2020.util.service.OdroidMessage;

import java.io.IOException;

public class Odroid {

    private static final String catagory = "Odroid";

    private Server server;
    private String address;
    private int port;

    private String foo = null;
    private int bar = 0;

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
                    if (!message.getClass().equals(OdroidMessage.class)) {
                        return;
                    }

                    // this is what it would look like except with actual messages instead.
                    if (((OdroidMessage) message).type == 0) {
                        if (((OdroidMessage) message).message.getClass().equals(String.class)) {
                            foo = (String) ((OdroidMessage) message).message;
                        }
                    }
                    if (((OdroidMessage) message).type == 1) {
                        if (((OdroidMessage) message).message.getClass().equals(Integer.class)) {
                            bar = (Integer) ((OdroidMessage) message).message;
                        }
                    }
                }
            });
            server.start();
            server.bind(port);
            Log.info(catagory, "Started server");
        } catch (IOException exception) {
            Log.error(catagory, "Failed to start server", exception);
        }
    }

    // The idea for this would be to put everything that is received into categories and each category
    // to be a queue (maybe not a queue, maybe just a object that gets swapped each time idk this is just
    // prototyping). Then when you query something in HardwareReader it will get and remove the first
    // element of the queue (or just whatever the object is). Idk, that seems like the best bet, problem is
    // atm is idk what needs to be received. So for now these will just be foo and bar.
    public int getBar() {
        return bar;
    }

    public String getFoo() {
        return foo;
    }
}
