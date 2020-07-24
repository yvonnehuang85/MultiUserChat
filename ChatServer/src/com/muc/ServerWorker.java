package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        //Create the function to read data from a client and sent data back to client
        //getting access to inputstream for reading data
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        //in order to read line by line
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            if ("quit".equalsIgnoreCase(line)){
                break;
            }
            String msg = "Enter:" + line + "\n";
            outputStream.write(msg.getBytes());
        }

        clientSocket.close();
    }
}
