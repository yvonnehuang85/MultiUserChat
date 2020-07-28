package com.muc;

//When someone send you a message is basically the other users will send the message
//to the server and server will relate that message to targeted user
public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
}
