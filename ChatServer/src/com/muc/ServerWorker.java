package com.muc;

import java.io.*;
import java.net.Socket;
import org.apache.commons.lang3.StringUtils;
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
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length != 0){
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line)){
                    break;
                }
                else{
                    String msg = "Wrong:" + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }
}
