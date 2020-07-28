package com.muc;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 8818);

        //before doing anything --> add user listener
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: " + login);
            }
        });

        //Connection to server   so we need a connection to a socket
        if (!client.connect()) {
            System.err.println("connection failed");
        } else {
            System.out.println("connection successful");
            if (client.login("guest", "guest")) {
                System.out.println("Login successful");
            } else {
                System.err.println("Login Failed");
            }
            client.logoff();
        }
    }

    private void logoff() throws IOException {
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }


    private boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());        //send command to the server

        String response = bufferedIn.readLine();
        System.out.println("Response Line : " + response);
        if ("successfully login".equalsIgnoreCase(response)) {
            startMsgReader();
            return true;
        } else {
            return false;
        }
    }

    private void startMsgReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMsgLoop();
            }
        };
        t.start();
    }

    //Read line by line from server output and it is also our client input
    private void readMsgLoop() {
        try {
            String line;
            while ((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line, null, 3);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)){
                        handleOnline(tokens);
                    }else if("offline".equalsIgnoreCase(cmd)){
                        handleOffline(tokens);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    //Create a method that component can register
    private void addUserStatusListener(UserStatusListener listener) {
        userStatusListeners.add(listener);
    }

    private void removeUserStatusListener(UserStatusListener listener) {
        userStatusListeners.remove(listener);
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners){
            listener.online(login);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners){
            listener.offline(login);
        }
    }
}


