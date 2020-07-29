package com.muc;

import javax.swing.*;
import java.awt.*;

//using java swing
public class UserListPane extends JPanel implements UserStatusListener {

    private final ChatClient client;
    private JList<String> userListUI;                   //Component

    public UserListPane(ChatClient client) {
        this.client = client;
        //Add itself to present listener
        //It means that this interface has to be implemented by this class.
        this.client.addUserStatusListener(this);

        userListUI = new JList<>();
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8818);

        UserListPane userListPane = new UserListPane(client);
        JFrame frame = new JFrame("User List");                  //display this pane to the windows
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //use system exit to get out of the application
        frame.setSize(400, 600);                        //400*600 pixel

        //userListPane is the main component --- frame.getContentPane().add(childComponent)
        frame.getContentPane().add(userListPane, BorderLayout.CENTER);
        frame.setVisible(true);                                        //must exist or you cannot see the window
    }

    @Override
    public void online(String login) {

    }

    @Override
    public void offline(String login) {

    }
}
