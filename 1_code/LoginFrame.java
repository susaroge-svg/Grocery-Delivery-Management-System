/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;


import java.awt.GridLayout;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Grocery Delivery Management System - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(new JLabel());
        add(loginButton);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = DataStore.authenticate(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid login credentials.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Login successful: " + user.getName());

        if (user.getRole().equals("CUSTOMER")) {
            new CustomerFrame(user).setVisible(true);
        } else if (user.getRole().equals("MANAGER")) {
            new ManagerFrame(user).setVisible(true);
        } else if (user.getRole().equals("DELIVERY")) {
            new DeliveryFrame(user).setVisible(true);
        }

        dispose();
    }
}
