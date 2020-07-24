package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private final int serverPort;

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            //continuously accept the connections from the client
            //if you are processing the connection of client, you cannot accept more connection
            //Keep the main thread free to accept other connection
            while (true) {
                System.out.println("Prepare to accept client connection");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection" + clientSocket);
                ServerWorker worker = new ServerWorker(clientSocket);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
