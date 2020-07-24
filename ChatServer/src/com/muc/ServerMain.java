package com.muc;


import java.io.IOException;


//network Server
public class ServerMain {
    public static void main(String[] args) {
        int port = 8818;
        Server server = new Server(port);
        server.start();

    }


}
