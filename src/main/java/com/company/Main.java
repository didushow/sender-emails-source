package com.company;

public class Main {

    public static void main(String[] args) {

        ThreadPooledServer server = new ThreadPooledServer(10001);
        new Thread(server).start();
    }
}
