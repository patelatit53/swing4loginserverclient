package com.rpise;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import javax.swing.*;

public class Client {

    public static void main(String[] args) {

        JFrame f1 = new JFrame("Login");
        JLabel l1 = new JLabel("Username");
        JLabel l2 = new JLabel("Password");
        JTextField tf1 = new JTextField(10);
        JPasswordField tf2 = new JPasswordField(10);
        JButton b1 = new JButton("Submit");
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        p1.add(l1);
        p1.add(tf1);
        p2.add(l2);
        p2.add(tf2);
        p3.add(b1);
        f1.add(p1, BorderLayout.NORTH);
        f1.add(p2, BorderLayout.CENTER);
        f1.add(p3, BorderLayout.SOUTH);
        f1.setSize(300, 300);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Socket soc = new Socket("127.0.0.1", 9081);
            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    soc.getOutputStream()
                            )
                    ), true
            );
            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(
                            soc.getInputStream()
                    ));
            L1 l = new L1(tf1, tf2, nos);
            b1.addActionListener(l);
            tf2.addActionListener(l);
            String str = nis.readLine();
            int counter = 0;
            while (!str.equals("true")) {
                JOptionPane.showMessageDialog(f1, "Enter valid Username Password");
                counter++;
                if (counter >= 3) {
                    tf1.setVisible(false);
                    tf2.setVisible(false);
                    b1.setEnabled(false);
                    JOptionPane.showMessageDialog(f1, "Blocked!!!");
                }
                str = nis.readLine();
            }
            f1.dispose();
            String name = l.getName();
            JOptionPane.showMessageDialog(null, "Login succesfull\n Welcome " + name);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(f1, "Connection Refused");
            f1.setVisible(false);
        }
    }
}



class L1 implements ActionListener {

    private JTextField tf1;
    private JTextField tf2;
    private PrintWriter nos;
    private String name;

    L1(JTextField tf1, JTextField tf2, PrintWriter nos) {
        this.tf1 = tf1;
        this.tf2 = tf2;
        this.nos = nos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        name = tf1.getText();
        String pwd = tf2.getText();
        nos.println(name);
        nos.println(pwd);
        tf1.setText("");
        tf2.setText("");
    }

    public String getName() {
        return this.name;
    }

}