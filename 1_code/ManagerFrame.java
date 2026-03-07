/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;


import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.*;

public class ManagerFrame extends JFrame {
    private User manager;
    private DefaultListModel<Order> orderModel;
    private JList<Order> orderList;
    private JComboBox<User> driverComboBox;
    private JButton assignButton;
    private JButton refreshButton;

    public ManagerFrame(User manager) {
        this.manager = manager;

        setTitle("Manager Dashboard - " + manager.getName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        orderModel = new DefaultListModel<>();
        orderList = new JList<>(orderModel);
        add(new JScrollPane(orderList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        driverComboBox = new JComboBox<>();
        loadDrivers();

        assignButton = new JButton("Assign Delivery");
        refreshButton = new JButton("Refresh Orders");

        bottomPanel.add(new JLabel("Select Driver:"));
        bottomPanel.add(driverComboBox);
        bottomPanel.add(assignButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        assignButton.addActionListener(e -> assignDelivery());
        refreshButton.addActionListener(e -> loadUnassignedOrders());

        loadUnassignedOrders();
    }

    private void loadDrivers() {
        driverComboBox.removeAllItems();
        for (User driver : DataStore.getDrivers()) {
            driverComboBox.addItem(driver);
        }
    }

    private void loadUnassignedOrders() {
        orderModel.clear();
        ArrayList<Order> unassigned = DataStore.getUnassignedOrders();
        for (Order order : unassigned) {
            orderModel.addElement(order);
        }
    }

    private void assignDelivery() {
        Order selectedOrder = orderList.getSelectedValue();
        User selectedDriver = (User) driverComboBox.getSelectedItem();

        if (selectedOrder == null) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        if (selectedDriver == null) {
            JOptionPane.showMessageDialog(this, "Select a driver first.");
            return;
        }

        Delivery delivery = new Delivery(DataStore.getNextDeliveryId(), selectedOrder, selectedDriver);
        DataStore.deliveries.add(delivery);
        selectedOrder.setStatus("Assigned");

        JOptionPane.showMessageDialog(this,
                "Order #" + selectedOrder.getOrderId() + " assigned to " + selectedDriver.getName());

        loadUnassignedOrders();
    }
}
