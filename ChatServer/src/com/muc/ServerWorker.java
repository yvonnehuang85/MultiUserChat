package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;

    public ServerWorker(Server server,Socket clientSocket) {
        this.server = server;
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
        this.outputStream = clientSocket.getOutputStream();

        //in order to read line by line
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length != 0){
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line)){
                    break;
                }else if("login".equalsIgnoreCase(cmd)){            //if we type login -> <user> <password>
                    handleLogin(outputStream, tokens);
                }else{
                    String msg = "Wrong: username or password\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }

    public String getLogin(){
        return login;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3){
            String username = tokens[1];
            String password = tokens[2];
            if ("guest".equalsIgnoreCase(username) && "guest".equalsIgnoreCase(password)){
                outputStream.write("successfully login \n".getBytes());

                this.login = username;
                System.out.println(login + " successfully login");

                String onlineInfo = login + " is online.";
                List<ServerWorker> workerList = server.getWorkerList();
                for(ServerWorker i : workerList){
                    i.send(onlineInfo);
                }
            }else{
                outputStream.write("error login\n".getBytes());
            }

        }
    }

    private void send(String onlineInfo) {
        //access the outputStream

    }
}
