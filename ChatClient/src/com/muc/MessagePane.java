package com.muc;

import javax.swing.*;
import java.awt.*;

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

    }
}
