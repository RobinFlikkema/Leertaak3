package com.example.leertaak_three;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * This is, obviously, the main class which is used to run the application
 */
public class Main {
    // The port used for receiving weatherdata
    private static final int SERVER_PORT = 7789;

    public static void main(String[] args)
            throws IOException {
        new WeatherdataService(new ServerSocket(SERVER_PORT));
    }
}