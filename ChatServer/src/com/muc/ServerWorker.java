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
                if ("quit".equalsIgnoreCase(line) || "logoff".equalsIgnoreCase(line)){
                    handleLogoff();
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
            if (("guest".equalsIgnoreCase(username) && "guest".equalsIgnoreCase(password)) || ("elsa".equalsIgnoreCase(username) && "elsa".equalsIgnoreCase(password))){
                outputStream.write("successfully login \n".getBytes());

                this.login = username;
                System.out.println(login + " successfully login");

                List<ServerWorker> workerList = server.getWorkerList();
                //get information from other users except itself
                for(ServerWorker i : workerList){
                    if(!login.equals(i.getLogin())) {
                        if (i.getLogin() != null) {
                            send(i.getLogin() + " is online.");
                        }
                    }
                }

                //send to other user that you are login
                String onlineInfo = "Other User :"+ login + " is online now.";
                for(ServerWorker i : workerList){
                    if(!login.equals(i.getLogin())) {
                        if(i.getLogin()!=null){
                            i.send(onlineInfo);         //if we take out i
                                                        //will send to itself (Other user: itself is online)
                        }
                    }
                }
            }else{
                outputStream.write("error login\n".getBytes());
            }

        }
    }

    private void handleLogoff() throws IOException {
        server.handleRemove(this);
        List<ServerWorker> workerList = server.getWorkerList();
        //send to other user that you are logoff
        String offlineInfo = "Other User :"+ login + " is offline now.";
        for(ServerWorker i : workerList){
            if(!login.equals(i.getLogin())) {
                i.send(offlineInfo);             //logoff means that thread will be null but we did not remove it from our list
            }
        }
        clientSocket.close();
    }

    //name as msg to be more general
    private void send(String msg) throws IOException {
        //access the outputStream
        if(login != null){
            outputStream.write((msg + "\n").getBytes());
        }
    }
}
