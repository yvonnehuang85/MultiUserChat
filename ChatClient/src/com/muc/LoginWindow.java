package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("login");

    public LoginWindow(){
        super("Login");             //frame's title

        this.client = new ChatClient("localhost", 8818);
        client.connect();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        //BoxLayout(Container target, axis)
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);

        //Create the action that when user click login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        //Add this panel to this windows(frame)
        //add "component" by "kind of layout"
        getContentPane().add(p, BorderLayout.CENTER);

        //resize the panel to fit the frame
        pack();
        setVisible(true);
    }

    private void doLogin() {
        String login = loginField.getText();
        String password = String.valueOf(passwordField.getPassword());
        //String password = passwordField.getText();

        try{
            if(client.login(login,password)){
                //bring up the user list window
                UserListPane userListPane = new UserListPane(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 600);

                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.setVisible(true);

                //close this window
                setVisible(false);

            }else{
                //show error
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
    }
}
