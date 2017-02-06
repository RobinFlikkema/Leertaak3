package com.example.leertaak_three;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * This thread accept a incoming connection and puts every Weatherdata as a List of Strings on to a Queue
 *
 * @author Robin Flikkema
 */
class ReceiverThread implements Runnable {
    // This bufferedReader is used to buffer the inputStreamReader (this results in less CPU usage)
    private BufferedReader bufferedReader = null;
    // This is used to pass Lists of Strings onto the next Thread
    private final BlockingQueue<ArrayList<String>> queue;
    // The incoming socket.
    private Socket socket;

    /**
     * @param socket, incoming Socket (from WeatherdataService)
     * @param queue, outgoing Queue of Lists of Strings.
     * The Buffers are limited to 256 to make sure we don't use too much memory
     */
    ReceiverThread(Socket socket, BlockingQueue<ArrayList<String>> queue) {
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream(), 256), "UTF-8"), 256);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = queue;
        this.socket = socket;
    }

    /**
     * This just runs the ReceiveWeatherdata method :D
     */
    @Override public void run() {
        try {
            this.receiveWeatherdata();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function, as the name suggests, receives weatherData from an incoming socket. After 161 lines the data is
     * sent off to the next Thread. (161 lines is the length of a Weatherdata object)
     *
     * @throws IOException, when an IO error occurs when using ReadLine()
     */
    private void receiveWeatherdata()
            throws IOException {
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
        } finally {
            bufferedReader.close();
            socket.close();
        }
    }
}
