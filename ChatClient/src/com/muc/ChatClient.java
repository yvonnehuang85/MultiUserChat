package com.muc;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;

public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 8818);
        //Connection to server   so we need a connection to a socket
        if(!client.connect()){
            System.err.println("connection failed");
        } else{
            System.out.println("connection successful");
            if (client.login("guest", "guest")){
                System.out.println("Login successful");
            }else{
                System.err.println("Login Failed");
            }
        }
    }

    private boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());        //send command to the server

        String response = bufferedIn.readLine();
        System.out.println("Response Line : " + response);
        if ("successfully login".equalsIgnoreCase(response)){
            return true;
        }else{
            return false;
        }
    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("The client port is " + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
