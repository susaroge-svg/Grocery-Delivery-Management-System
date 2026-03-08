/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;


public class CustomerFrame extends JFrame {
    private User customer;
    private JTextField searchField;
    private JButton searchButton;
    private JButton showAllButton;
    private JButton addToCartButton;
    private JButton placeOrderButton;
    private JButton refreshOrdersButton;
    private JButton openManagerDemoButton;
    private JButton openDriverDemoButton;

    private DefaultListModel<Product> productModel;
    private JList<Product> productList;

    private JTextArea cartArea;
    private JTextArea orderHistoryArea;

    private ArrayList<OrderItem> cart;

    public CustomerFrame(User customer) {
        this.customer = customer;
        this.cart = new ArrayList<>();

        setTitle("Customer Dashboard - " + customer.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        searchField = new JTextField();
        searchButton = new JButton("Search");
        showAllButton = new JButton("Show All");
        addToCartButton = new JButton("Add To Cart");

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(showAllButton);
        topPanel.add(addToCartButton);

        add(topPanel, BorderLayout.NORTH);

        productModel = new DefaultListModel<>();
        productList = new JList<>(productModel);
        loadProducts(DataStore.products);

        JScrollPane productScroll = new JScrollPane(productList);

        cartArea = new JTextArea();
        cartArea.setEditable(false);
        JScrollPane cartScroll = new JScrollPane(cartArea);

        orderHistoryArea = new JTextArea();
        orderHistoryArea.setEditable(false);
        JScrollPane historyScroll = new JScrollPane(orderHistoryArea);

        JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cartScroll, historyScroll);
        bottomSplit.setDividerLocation(400);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, productScroll, bottomSplit);
        mainSplit.setDividerLocation(250);

        add(mainSplit, BorderLayout.CENTER);

        placeOrderButton = new JButton("Place Order");
        refreshOrdersButton = new JButton("Refresh Order History");
        openManagerDemoButton = new JButton("Open Manager Demo");
        openDriverDemoButton = new JButton("Open Driver Demo");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(placeOrderButton);
        bottomPanel.add(refreshOrdersButton);
        bottomPanel.add(openManagerDemoButton);
        bottomPanel.add(openDriverDemoButton);

        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchProducts());
        showAllButton.addActionListener(e -> loadProducts(DataStore.products));
        addToCartButton.addActionListener(e -> addSelectedProductToCart());
        placeOrderButton.addActionListener(e -> placeOrder());
        refreshOrdersButton.addActionListener(e -> refreshOrderHistory());
        openManagerDemoButton.addActionListener(e -> openManagerDemo());
        openDriverDemoButton.addActionListener(e -> openDriverDemo());

        refreshCartDisplay();
        refreshOrderHistory();
    }

    private void loadProducts(ArrayList<Product> products) {
        productModel.clear();
        for (Product p : products) {
            productModel.addElement(p);
        }
    }

    private void searchProducts() {
        String keyword = searchField.getText().trim().toLowerCase();
        DefaultListModel<Product> filtered = new DefaultListModel<>();

        for (Product p : DataStore.products) {
            if (p.getName().toLowerCase().contains(keyword)
                    || p.getCategory().toLowerCase().contains(keyword)) {
                filtered.addElement(p);
            }
        }

        productList.setModel(filtered);
    }

    private void addSelectedProductToCart() {
        Product selected = productList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }

        if (selected.getStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Item is out of stock.");
            return;
        }

        String qtyInput = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (qtyInput == null) {
            return;
        }

        try {
            int qty = Integer.parseInt(qtyInput);

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }

            if (qty > selected.getStock()) {
                JOptionPane.showMessageDialog(this, "Not enough stock available.");
                return;
            }

            cart.add(new OrderItem(selected, qty));
            refreshCartDisplay();
            JOptionPane.showMessageDialog(this, "Item added to cart.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number.");
        }
    }

    private void refreshCartDisplay() {
        StringBuilder sb = new StringBuilder();
        double total = 0.0;

        if (cart.isEmpty()) {
            sb.append("Cart is empty.");
        } else {
            for (OrderItem item : cart) {
                sb.append(item).append("\n");
                total += item.getSubtotal();
            }
            sb.append("\nTotal: $").append(String.format("%.2f", total));
        }

        cartArea.setText(sb.toString());
    }

    private void placeOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        Orders order = new Orders(DataStore.getNextOrderId(), customer);

        for (OrderItem item : cart) {
            Product p = item.getProduct();
            p.setStock(p.getStock() - item.getQuantity());
            order.addItem(item);
        }

        DataStore.orders.add(order);
        cart.clear();

        refreshCartDisplay();
        refreshOrderHistory();
        loadProducts(DataStore.products);

        JOptionPane.showMessageDialog(this,
                "Order placed successfully.\nOrder Number: " + order.getOrderId()
                + "\nStatus: " + order.getStatus());
    }

    private void refreshOrderHistory() {
        ArrayList<Orders> customerOrders = DataStore.getOrdersForCustomer(customer);

        StringBuilder sb = new StringBuilder();

        if (customerOrders.isEmpty()) {
            sb.append("No orders placed yet.");
        } else {
            for (Orders order : customerOrders) {
                sb.append(order).append("\n");
                for (OrderItem item : order.getItems()) {
                    sb.append("   - ").append(item).append("\n");
                }
                sb.append("\n");
            }
        }

        orderHistoryArea.setText(sb.toString());
    }

    private void openManagerDemo() {
        User manager = DataStore.authenticate("manager@grocery.com", "password123");

        if (manager != null) {
            ManagerFrame managerFrame = new ManagerFrame(manager);
            managerFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Manager demo account not found.");
        }
    }

    private void openDriverDemo() {
        User driver = DataStore.authenticate("driver@grocery.com", "password123");

        if (driver != null) {
            DeliveryFrame deliveryFrame = new DeliveryFrame(driver);
            deliveryFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Driver demo account not found.");
        }
    }
}