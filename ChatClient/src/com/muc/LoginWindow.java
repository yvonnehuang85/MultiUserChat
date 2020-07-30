package com.muc;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoginWindow extends JFrame {
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("login");

    public LoginWindow(){
        super("Login");             //frame's title

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        //BoxLayout(Container target, axis)
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);

        //Add this panel to this windows(frame)
        //add "component" by "kind of layout"
        getContentPane().add(p, BorderLayout.CENTER);

        //resize the panel to fit the frame
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
    }
}
