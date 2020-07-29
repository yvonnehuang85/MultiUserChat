package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class MessagePane extends JPanel {

    private final ChatClient client;
    private final String login;

    //Start create the messagePane user interface
    private final DefaultListModel<String> msgListModel;
    private JList<String> msgList;
    private JTextField inputField = new JTextField();

    public MessagePane(ChatClient client, String login) {
        this.client = client;
        this.login = login;

        msgListModel = new DefaultListModel<>();
        msgList = new JList<>(msgListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(msgList), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        //Add an event listener to this input field
        //Action Event Class deal with the event that when you press the button
        //Action Listener is an interface, the parameter e is the object of action event
        //In this case: if you enter the msg, call actionPerformed
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send msg to the client
                try {
                    String text = inputField.getText();
                    client.msg(login, text);
                    //Add the text to conversation list (addElement---add at the end of the vector ans increase the sze by one)
                    msgListModel.addElement(text);
                    //clear the inputField
                    inputField.setText("");

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
    }
}
