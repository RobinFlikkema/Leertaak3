package com.example.leertaak_three;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Robin on 8-12-2016.
 */
class ReceiverThread implements Runnable {
    private BufferedReader bufferedReader = null;
    private final BlockingQueue<ArrayList<String>> queue;

    ReceiverThread(Socket socket, BlockingQueue<ArrayList<String>> queue) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream()), "UTF-8"), 256);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = queue;
    }

    @Override public void run() {
        this.receiveWeatherdata();
    }

    // TODO: Needs refactoring
    private void receiveWeatherdata() {
        try {
            ArrayList<String> incomingList = new ArrayList<>();
            int lineCounter = 0;
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    incomingList.add(line);
                    if (lineCounter > 161) {
                        incomingList.add(line);
                        queue.add(incomingList);

                        incomingList = new ArrayList<>();
                        lineCounter = 0;
                    } else {
                        lineCounter++;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
