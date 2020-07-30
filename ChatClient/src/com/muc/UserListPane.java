package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

//using java swing
public class UserListPane extends JPanel implements UserStatusListener {

    private final ChatClient client;
    private JList<String> userListUI;                   //Component
    private DefaultListModel<String> userListModel;

    public UserListPane(ChatClient client) {
        this.client = client;
        //Add itself to present listener
        //It means that this interface has to be implemented by this class.
        this.client.addUserStatusListener(this);

        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.CENTER);

        //Add an event listener to the list
        //So when a user clicked on that list, we will be able ot figure out which login they click on
        //and from that we can create a message pane window with that login
        //so messages sent from that window will be targeted to the login
        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()>1){
                    String login = userListUI.getSelectedValue();               //figure out which row we click on
                    MessagePane messagePane = new MessagePane(client, login);   //create a message pane for that login and pass in client

                    //show it as separate window
                    JFrame f = new JFrame("Message " + login);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setSize(400, 400);

                    f.getContentPane().add(messagePane, BorderLayout.CENTER);
                    f.setVisible(true);
                }
            }
        });
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

        if(client.connect()){
            try {
                client.login("guest","guest");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void online(String login) {
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }
}
