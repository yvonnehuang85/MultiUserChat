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
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        try {
                            handleClientSocket(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                };
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleClientSocket(Socket clientSocket) throws IOException, InterruptedException {
        OutputStream outputStream = clientSocket.getOutputStream();
        //loop 10 times for 10 seconds
        for(int i=0; i<10; i++) {
            outputStream.write(("Now" + new Date() + "\n").getBytes());
            Thread.sleep(1000);
        }
        clientSocket.close();
    }
}
