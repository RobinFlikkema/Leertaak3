package com.example.leertaak_three;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Robin on 8-12-2016.
 */
class ReceiverThread implements Runnable {
    private BufferedReader bufferedReader = null;
    private BlockingQueue<ArrayList<String>> queue;

    ReceiverThread(Socket socket, BlockingQueue<ArrayList<String>> queue) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream()), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = queue;
    }

    @Override public void run() {
        while (true) {
            if (!this.receiveWeatherdata()) {
                break;
            }
        }
    }

    // TODO: Needs refactoring
    private Boolean receiveWeatherdata() {
        try {
            ArrayList<String> incomingList = new ArrayList<>();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    incomingList.add(line);
                    if (line.equals("</WEATHERDATA>")) {
                        incomingList.add(line);
                        queue.add(incomingList);
                        incomingList = new ArrayList<>();
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
