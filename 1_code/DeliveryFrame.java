/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.*;

public class DeliveryFrame extends JFrame {
    private User driver;
    private DefaultListModel<Delivery> deliveryModel;
    private JList<Delivery> deliveryList;
    private JButton outForDeliveryButton;
    private JButton deliveredButton;
    private JButton refreshButton;

    public DeliveryFrame(User driver) {
        this.driver = driver;

        setTitle("Delivery Dashboard - " + driver.getName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        deliveryModel = new DefaultListModel<>();
        deliveryList = new JList<>(deliveryModel);

        add(new JScrollPane(deliveryList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        outForDeliveryButton = new JButton("Mark Out for Delivery");
        deliveredButton = new JButton("Mark Delivered");
        refreshButton = new JButton("Refresh");

        bottomPanel.add(outForDeliveryButton);
        bottomPanel.add(deliveredButton);
        bottomPanel.add(refreshButton);

        add(bottomPanel, BorderLayout.SOUTH);

        outForDeliveryButton.addActionListener(e -> updateStatus("Out for Delivery"));
        deliveredButton.addActionListener(e -> updateStatus("Delivered"));
        refreshButton.addActionListener(e -> loadDeliveries());

        loadDeliveries();
    }

    private void loadDeliveries() {
        deliveryModel.clear();
        ArrayList<Delivery> deliveries = DataStore.getDeliveriesForDriver(driver);
        for (Delivery d : deliveries) {
            deliveryModel.addElement(d);
        }
    }

    private void updateStatus(String newStatus) {
        Delivery selected = deliveryList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a delivery first.");
            return;
        }

        selected.setStatus(newStatus);
        selected.getOrder().setStatus(newStatus);

        JOptionPane.showMessageDialog(this,
                "Order #" + selected.getOrder().getOrderId() + " updated to: " + newStatus);

        loadDeliveries();
    }
}
