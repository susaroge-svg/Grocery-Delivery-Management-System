/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GroceryDeliveryManagementSystem;



import java.util.ArrayList;

public class Orders {
    private int orderId;
    private User customer;
    private ArrayList<OrderItem> items;
    private String status;
    private double total;

    public Orders(int orderId, User customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.status = "Processing";
        this.total = 0.0;
    }

    public int getOrderId() {
        return orderId;
    }

    public User getCustomer() {
        return customer;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public double getTotal() {
        return total;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        total += item.getSubtotal();
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " | Customer: " + customer.getName()
                + " | Status: " + status + " | Total: $" + String.format("%.2f", total);
    }
}
