package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws IOException {
        BlockingQueue<Weatherdata> weatherdata_queue = new LinkedBlockingQueue<>();

        new Database(weatherdata_queue);
        new WeatherdataService(new ServerSocket(7789), weatherdata_queue);
    }
}