package com.muc;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//network Server
public class ServerMain {
    public static void main(String[] args) throws IOException {
        int port = 8818;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
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
