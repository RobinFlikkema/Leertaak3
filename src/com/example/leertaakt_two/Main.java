package com.example.leertaakt_two;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        new WeatherdataService(new ServerSocket(7789));
    }
}