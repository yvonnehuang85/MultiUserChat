package com.muc;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private Hashtable<String, String> dict = new Hashtable<>();
    private HashSet<String> topicSet = new HashSet<>();

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
        while((line = reader.readLine()) != null){
            String[] tokens = StringUtils.split(line,null,3);
            if (tokens != null && tokens.length > 0){
                // set the token[0] is the command
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(line) || "logoff".equalsIgnoreCase(line)){
                    handleLogoff();
                    break;
                }else if("login".equalsIgnoreCase(cmd)) {            //if we type login -> <user> <password>
                    handleLogin(outputStream, tokens, dict);
                }else if("msg".equalsIgnoreCase(cmd)){
                    handleMsg(tokens);
                }else if("join".equalsIgnoreCase(cmd)){
                    handleJoin(tokens);
                }else if("leave".equalsIgnoreCase(cmd)){
                    handleLeave(tokens);
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

    //classify: login will be the person who is sending message now
    //          i.getLogin() will be people in the workerList, it means that people who are online now or who are in the team

    private boolean checkExist() throws IOException {
        List<ServerWorker> workerList = server.getWorkerList();
        int count = 0;
        for(ServerWorker i : workerList){
            if(login.equals(i.getLogin())) {
                count += 1;
            }
        }
        if(count > 1) {
            return true;
        }
        return false;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens, Hashtable<String, String> dict) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            //System.out.println(dict);


            //System.out.println(workerList);
            /*for(ServerWorker i : workerList){
                if(login.equals(i.getLogin())) {
                    outputStream.write("username has been taken\n".getBytes());
                    System.err.println("Login failed for " + login + "username has been taken\n");
                }
            }*/

            List<ServerWorker> workerList = server.getWorkerList();
            int count = 0;
            for(ServerWorker i : workerList){
                if(login.equals(i.getLogin())) {
                    count += 1;
                }
            }
            System.out.println(count);
            if(count == 1) {
                outputStream.write("username has been taken\n".getBytes());
                System.err.println("Login failed for " + login + "username has been taken/n");
            }else {
                dict.put(login, password);
                outputStream.write("successfully login\n".getBytes());

                this.login = login;
                System.out.println(login + " successfully login");


                //get information from other users except itself
                //the person who log in    call this send
                //List<ServerWorker> workerList = server.getWorkerList();
                for(ServerWorker i : workerList){
                    if(!login.equals(i.getLogin())) {
                        if (i.getLogin() != null) {
                            send("online " + i.getLogin());
                        }
                    }
                }

                //send to other user that you are login
                //i    call  this send (i is people who already login)
                String onlineInfo = "online "+ login;
                for(ServerWorker i : workerList){
                    if(!login.equals(i.getLogin())) {
                        if(i.getLogin()!=null){
                            i.send(onlineInfo);         //if we take out i
                            //will send to itself (Other user: itself is online)
                        }
                    }
                }
            }




            /*if ((login.equals("guest") && password.equals("guest")) || (login.equals("elsa") && password.equals("elsa"))){
            outputStream.write("successfully login\n".getBytes());

            this.login = login;
            System.out.println(login + " successfully login");

            List<ServerWorker> workerList = server.getWorkerList();
            //get information from other users except itself
            //the person who log in    call this send
            for(ServerWorker i : workerList){
                if(!login.equals(i.getLogin())) {
                    if (i.getLogin() != null) {
                        send("online " + i.getLogin());
                    }
                }
            }

            //send to other user that you are login
            //i    call  this send (i is people who already login)
            String onlineInfo = "online "+ login;
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
            System.err.println("Login failed for " + login);
            }
        }*/
        /*else{
            outputStream.write("error login\n".getBytes());
            System.err.println("Login failed for " + login);
        }*/
    }
}

    private void handleLogoff() throws IOException {
        server.handleRemove(this);
        List<ServerWorker> workerList = server.getWorkerList();
        //send to other user that you are logoff
        String offlineInfo = "offline "+ login;
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

    //handle msg commands
    //guest: "msg" "elsa" "Hello World"  ---> Sent
    //else:  "msg" "guest" "Hello World" ---> Receive
    private void handleMsg(String[] tokens) throws IOException {
        String sendTo = tokens[1];              //also is the topic in join command
        String body = tokens[2];

        boolean isTopic = sendTo.charAt(0) == '#';

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker i : workerList){
            //chatroom send message to the chatroom with the sender_name
            if (isTopic){
                if (i.isMemberOfTopic(sendTo)){
                    String sendMsg = "msg " + sendTo + ":" + login + " " + body + "\n";
                    i.send(sendMsg);
                }
            }
            //not chat room
            else if (sendTo.equalsIgnoreCase(i.getLogin())){
                String sendMsg = "msg " + login + " " + body + "\n";
                i.send(sendMsg);
            }
        }
    }

    private void handleJoin(String[] tokens){
        if(tokens.length>1){
            String topic = tokens[1];
            //store membership to the topic
            topicSet.add(topic);
        }
    }

    //Define a function to see if that topic string in inside the topicSet
    private boolean isMemberOfTopic(String topic){
        return topicSet.contains(topic);
    }


    private void handleLeave(String[] tokens) {
        if(tokens.length>1){
            String topic = tokens[1];
            //store membership to the topic
            topicSet.remove(topic);
        }
    }

}
